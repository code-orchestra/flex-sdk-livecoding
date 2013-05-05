package codeOrchestra;

import codeOrchestra.digest.DigestManager;
import codeOrchestra.digest.IClassDigest;
import codeOrchestra.digest.IMember;
import codeOrchestra.digest.ITypeResolver;
import codeOrchestra.digest.impl.SWCMember;
import codeOrchestra.digest.impl.SourceClassDigest;
import codeOrchestra.digest.impl.SourceMember;
import codeOrchestra.tree.TreeNavigator;
import codeOrchestra.tree.TreeUtil;
import codeOrchestra.tree.visitor.NodeVisitor;
import codeOrchestra.tree.visitor.NodeVisitorFactory;
import codeOrchestra.util.ObjectUtils;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.*;
import macromedia.asc.util.ObjectList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anton.I.Neverov
 * @author Alexander Eliseyev
 */
public class LCIncrementalExtension extends AbstractTreeModificationExtension {

    public static long lastCompilationStartTime;

    private final String fqName;
    private final String initialClassName;
    private final String initialPackageName;

    public LCIncrementalExtension(String fqClassName) {
        fqName = fqClassName;

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
        long l = System.currentTimeMillis();

        ClassDefinitionNode classDefinitionNode = TreeNavigator.getClassDefinition(unit);
        String className = classDefinitionNode.name.name;
        if (!(initialPackageName.equals(classDefinitionNode.pkgdef.name.id.pkg_part) && initialClassName.equals(className))) {
            return;
        }

        loadSyntaxTrees();
        saveSyntaxTree(unit);
        ProgramNode syntaxTree = projectNavigator.getSyntaxTree(classDefinitionNode.pkgdef.name.id.pkg_part, className);
        ClassDefinitionNode originalClass = TreeNavigator.getPackageClassDefinition(syntaxTree);

        List<FunctionDefinitionNode> changedMethods = findChangedMethods(originalClass, classDefinitionNode);
        if (TRACE) {
            System.out.println("Comparison of files took " + (System.currentTimeMillis() - l) + "ms");
        }

        ArrayList<String> liveCodingClassNames = new ArrayList<String>();
        Map<FunctionDefinitionNode, String> functionToClassNames = new HashMap<FunctionDefinitionNode, String>();
        for (FunctionDefinitionNode changedMethod : changedMethods) {
            ObjectList<Node> oldBody = changedMethod.fexpr.body.items;
            changedMethod.fexpr.body.items = new ObjectList<Node>();
            String liveCodingClassName = addLiveCodingClass(className, classDefinitionNode, changedMethod, oldBody, true);
            functionToClassNames.put(changedMethod, liveCodingClassName);
            liveCodingClassNames.add(liveCodingClassName);
        }
        FunctionDefinitionNode constructor = TreeUtil.removeAllMethodsAndClearConstructor(classDefinitionNode);
        for (String liveCodingClassName : liveCodingClassNames) {
            constructor.fexpr.body.items.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier(liveCodingClassName), -1)));
            TreeUtil.addImport(unit, "codeOrchestra.liveCoding.load", liveCodingClassName);
        }

        System.out.println("Delivery Message: [" + new DeliveryMessageBuilder(fqName, changedMethods, functionToClassNames, classDefinitionNode).build() + "]");
    }

    /**
     * Returns first changed method
     * <p/>
     * We hope that method order remains unchanged
     */
    private List<FunctionDefinitionNode> findChangedMethods(ClassDefinitionNode originalClass, ClassDefinitionNode modifiedClass) {
        List<FunctionDefinitionNode> originalMethodDefinitions = TreeNavigator.getMethodDefinitions(originalClass);
        List<FunctionDefinitionNode> modifiedMethodDefinitions = TreeNavigator.getMethodDefinitions(modifiedClass);

        ArrayList<FunctionDefinitionNode> result = new ArrayList<FunctionDefinitionNode>();
        for (FunctionDefinitionNode modifiedMethod : modifiedMethodDefinitions) {
            // COLT-77
            if (LiveCodingUtil.hasAnnotation(modifiedMethod, LiveCodingUtil.LIVE_CODE_DISABLE_ANNOTATION)) {
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
                if (LiveCodingUtil.hasAnnotation(matchingOriginalMethod, LiveCodingUtil.LIVE_CODE_DISABLE_ANNOTATION)) {
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
                    boolean isStatic = TreeNavigator.isStaticMethod(modifiedMethod);
                    SourceMember newMember = new SourceMember(
                            modifiedMethod.fexpr.identifier.name,
                            TreeNavigator.getShortTypeName(modifiedMethod.fexpr.signature.result),
                            isStatic,
                            TreeNavigator.getMemberKind(modifiedMethod),
                            TreeNavigator.getVisibility(modifiedMethod),
                            classDigest);

                    List<IMember> membersList;
                    if (isStatic) {
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
