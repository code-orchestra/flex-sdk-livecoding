package codeOrchestra;

import codeOrchestra.digest.*;
import codeOrchestra.digest.impl.SourceClassDigest;
import codeOrchestra.digest.impl.SourceMember;
import codeOrchestra.tree.TreeNavigator;
import codeOrchestra.tree.TreeUtil;
import codeOrchestra.tree.visitor.NodeVisitor;
import codeOrchestra.tree.visitor.NodeVisitorFactory;
import codeOrchestra.util.ObjectUtils;
import codeOrchestra.util.Pair;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.*;
import macromedia.asc.util.ObjectList;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

/**
 * @author Anton.I.Neverov
 * @author Alexander Eliseyev
 */
public class LCIncrementalExtension extends AbstractTreeModificationExtension {

    public static long lastCompilationStartTime;

    private final String initialClassName;
    private final String initialPackageName;

    public LCIncrementalExtension(String fqClassName) {
        if (fqClassName.contains(":")) {
            String[] parts = fqClassName.split(":");
            initialPackageName = parts[0];
            initialClassName = parts[1];
        } else {
            initialPackageName = "";
            initialClassName = fqClassName;
        }
    }

    @Override
    protected void performModifications(CompilationUnit unit) {
        ClassDefinitionNode modifiedClass = TreeNavigator.getClassDefinition(unit);
        if (modifiedClass == null) {
            return;
        }

        String className = modifiedClass.name.name;
        String fqName = TreeUtil.getFqName(modifiedClass);
        if (!(initialPackageName.equals(modifiedClass.pkgdef.name.id.pkg_part) && initialClassName.equals(className))) {
            return;
        }

        loadSyntaxTrees();
        saveSyntaxTree(unit);
        ProgramNode syntaxTree = projectNavigator.getSyntaxTree(modifiedClass.pkgdef.name.id.pkg_part, className);

        ClassDefinitionNode originalClass = syntaxTree != null ? TreeNavigator.getPackageClassDefinition(syntaxTree) : null;

        // COLT-213
        for (VariableDefinitionNode variableDefinitionNode : TreeNavigator.getFieldDefinitions(modifiedClass)) {
            TreeUtil.removeAnnotation(variableDefinitionNode, "Embed");
        }

        // COLT-171
        if (originalClass == null) {
            // New class is added
            SourceClassDigest classDigest = DigestManager.getInstance().addToDigestUnresolved(modifiedClass);
            classDigest.resolve();
            return;
        }

        ArrayList<String> liveCodingClassNames = new ArrayList<>();
        Map<FunctionDefinitionNode, String> functionToClassNames = new HashMap<>();

        List<FunctionDefinitionNode> changedMethods = findChangedMethods(originalClass, modifiedClass);
        List<VariableBindingNode> newFields = findNewFields(originalClass, modifiedClass);
        if (!newFields.isEmpty()) {
            FunctionDefinitionNode liveCodingInitializerMethod = addLiveInitializerMethod(modifiedClass, true, null);
            liveCodingInitializerMethod.fexpr.body.items = new ObjectList<Node>();

            ObjectList<Node> liveInitBody = liveCodingInitializerMethod.fexpr.body.items;
            IClassDigest classDigest = DigestManager.getInstance().getClassDigest(fqName);
            classDigest.getAllMembers().stream().filter(member -> member.getKind() == MemberKind.FIELD && member.isAddedDuringProcessing()).forEach(member -> {
                VariableBindingNode addedField = TreeNavigator.getFieldDefinition(member.getName(), modifiedClass);
                if (addedField.initializer != null) {
                    Node initializerClone = SerializationUtils.clone(addedField.initializer);
                    liveInitBody.add(
                            TreeUtil.createExpressionStatement(
                                    TreeUtil.createAssignmentExpression(
                                            member.isStatic() ? TreeUtil.createIdentifier(className) : new ThisExpressionNode(), new IdentifierNode(member.getName(), -1), new ArgumentListNode(initializerClone, -1))));
                }
            });
            liveInitBody.add(new ReturnStatementNode(null));

            changedMethods.add(liveCodingInitializerMethod);
        }

        for (FunctionDefinitionNode changedMethod : changedMethods) {
            ObjectList<Node> oldBody = changedMethod.fexpr.body.items;
            changedMethod.fexpr.body.items = new ObjectList<Node>();
            String liveCodingClassName = addLiveCodingClass(className, modifiedClass, changedMethod, oldBody, true);
            functionToClassNames.put(changedMethod, liveCodingClassName);
            liveCodingClassNames.add(liveCodingClassName);
        }
        FunctionDefinitionNode constructor = TreeUtil.removeAllMethodsAndClearConstructor(modifiedClass);
        for (String liveCodingClassName : liveCodingClassNames) {
            constructor.fexpr.body.items.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier(liveCodingClassName), -1)));
            TreeUtil.addImport(unit, "codeOrchestra.liveCoding.load", liveCodingClassName);
        }

        System.out.println("Delivery Message: [" + new DeliveryMessageBuilder(fqName, changedMethods, functionToClassNames, modifiedClass).build() + "]");
    }

    private List<VariableBindingNode> findNewFields(ClassDefinitionNode originalClass, ClassDefinitionNode modifiedClass) {
        List<Pair<VariableDefinitionNode, VariableBindingNode>> originalFieldDefinitions = TreeNavigator.getFieldDefinitionsVars(originalClass);
        List<Pair<VariableDefinitionNode, VariableBindingNode>> modifiedFieldDefinitions = TreeNavigator.getFieldDefinitionsVars(modifiedClass);

        ArrayList<VariableBindingNode> result = new ArrayList<>();
        for (Pair<VariableDefinitionNode, VariableBindingNode> modifiedField : modifiedFieldDefinitions) {
            Pair<VariableDefinitionNode, VariableBindingNode> matchingOriginalField = null;
            for (Pair<VariableDefinitionNode, VariableBindingNode> originalField : originalFieldDefinitions) {
                if (ObjectUtils.equals(originalField.getO2().variable.identifier.name, modifiedField.getO2().variable.identifier.name)) {
                    matchingOriginalField = originalField;
                    break;
                }
            }

            // Seems like we've added a new field
            if (matchingOriginalField == null) {
                IClassDigest classDigest = DigestManager.getInstance().getClassDigest(TreeUtil.getFqName(originalClass));
                if (classDigest != null && classDigest instanceof SourceClassDigest) {
                    SourceMember newMember = SourceMember.fromVariableBinding(modifiedField.getO2(), modifiedField.getO1(), classDigest);
                    newMember.setAddedDuringProcessing(true);

                    List<IMember> membersList;
                    if (newMember.isStatic()) {
                        membersList = classDigest.getStaticMembers();
                    } else {
                        membersList = classDigest.getInstanceMembers();
                    }

                    if (!membersList.contains(newMember)) {
                        membersList.add(newMember);
                        newMember.resolve((ITypeResolver) classDigest);
                    }

                    result.add(modifiedField.getO2());
                }
            }
        }

        return result;
    }

    private List<FunctionDefinitionNode> findChangedMethods(ClassDefinitionNode originalClass, ClassDefinitionNode modifiedClass) {
        LiveCodingPolicy liveCodingPolicy = LiveCodingUtil.getLiveCodingPolicy(originalClass);
        if (!liveCodingPolicy.isEnabled()) {
            return Collections.emptyList();
        }

        List<FunctionDefinitionNode> originalMethodDefinitions = TreeNavigator.getMethodDefinitions(originalClass);
        List<FunctionDefinitionNode> modifiedMethodDefinitions = TreeNavigator.getMethodDefinitions(modifiedClass);

        ArrayList<FunctionDefinitionNode> result = new ArrayList<>();
        for (FunctionDefinitionNode modifiedMethod : modifiedMethodDefinitions) {
            // COLT-77
            if (TreeNavigator.hasAnnotation(modifiedMethod, LiveCodingUtil.LIVE_CODE_DISABLE_ANNOTATION)) {
                continue;
            }
            // COLT-189
            if (!LiveCodingCLIParameters.makeGettersSettersLive() && (TreeNavigator.isGetter(modifiedMethod) || TreeNavigator.isSetter(modifiedMethod))) {
                continue;
            }
            if (liveCodingPolicy == LiveCodingPolicy.SELECTED_METHODS && !TreeNavigator.hasAnnotation(modifiedMethod, LiveCodingUtil.LIVE_ANNOTATION)) {
                continue;
            }

            FunctionDefinitionNode matchingOriginalMethod = null;
            for (FunctionDefinitionNode originalMethodCandidate : originalMethodDefinitions) {
                if (ObjectUtils.equals(originalMethodCandidate.fexpr.identifier.name, modifiedMethod.fexpr.identifier.name)
                        && originalMethodCandidate.name.kind == modifiedMethod.name.kind) {
                    matchingOriginalMethod = originalMethodCandidate;
                    break;
                }
            }

            if (matchingOriginalMethod != null) {
                // COLT-77
                if (TreeNavigator.hasAnnotation(matchingOriginalMethod, LiveCodingUtil.LIVE_CODE_DISABLE_ANNOTATION)) {
                    continue;
                }

                NodeVisitor<FunctionDefinitionNode> visitor = NodeVisitorFactory.getVisitor(FunctionDefinitionNode.class);
                if (!visitor.compareTrees(matchingOriginalMethod, modifiedMethod) && !result.contains(modifiedMethod)) {
                    result.add(modifiedMethod);
                }
            } else {
                // Seems like we've added a new method
                result.add(modifiedMethod);

                IClassDigest classDigest = DigestManager.getInstance().getClassDigest(TreeUtil.getFqName(originalClass));
                if (classDigest != null && classDigest instanceof SourceClassDigest) {
                    SourceMember newMember = SourceMember.fromFunctionDefinition(modifiedMethod, classDigest);
                    newMember.setAddedDuringProcessing(true);

                    List<IMember> membersList;
                    if (newMember.isStatic()) {
                        membersList = classDigest.getStaticMembers();
                    } else {
                        membersList = classDigest.getInstanceMembers();
                    }

                    if (!membersList.contains(newMember)) {
                        membersList.add(newMember);
                        newMember.resolve((ITypeResolver) classDigest);
                    }
                }
            }
        }

        return result;
    }

}
