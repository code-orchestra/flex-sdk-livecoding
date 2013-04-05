package codeOrchestra;

import codeOrchestra.tree.TreeNavigator;
import codeOrchestra.tree.TreeUtil;
import codeOrchestra.tree.visitor.NodeVisitor;
import codeOrchestra.tree.visitor.NodeVisitorFactory;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.*;
import macromedia.asc.util.ObjectList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anton.I.Neverov
 */
public class LCIncrementalExtension extends AbstractTreeModificationExtension {

    private final String fqName;
    private final String initialClassName;
    private final String initialPackageName;

    public LCIncrementalExtension(String fqClassName) {
        String[] parts = fqClassName.split(":");
        fqName = fqClassName;
        initialPackageName = parts[0];
        initialClassName = parts[1];
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
        ClassDefinitionNode originalClass = TreeNavigator.getClassDefinition(syntaxTree);

        List<FunctionDefinitionNode> changedMethods = findChangedMethods(originalClass, classDefinitionNode);
        if (TRACE) {
            System.out.println("Comparison of files took " + (System.currentTimeMillis() - l) + "ms");
        }

        ArrayList<String> liveCodingClassNames = new ArrayList<String>();
        Map<FunctionDefinitionNode, String> functionToClassNames = new HashMap<FunctionDefinitionNode, String>();
        for(FunctionDefinitionNode changedMethod : changedMethods) {
            ObjectList<Node> oldBody = changedMethod.fexpr.body.items;
            changedMethod.fexpr.body.items = new ObjectList<Node>();
            String liveCodingClassName = addLiveCodingClass(className, changedMethod, oldBody, true);
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
     *
     * We hope that method order remains unchanged
     */
    private List<FunctionDefinitionNode> findChangedMethods(ClassDefinitionNode originalClass, ClassDefinitionNode modifiedClass) {
        List<FunctionDefinitionNode> originalMethodDefinitions = TreeNavigator.getMethodDefinitions(originalClass);
        List<FunctionDefinitionNode> modifiedMethodDefinitions = TreeNavigator.getMethodDefinitions(modifiedClass);

        if (originalMethodDefinitions.size() != modifiedMethodDefinitions.size()) {
            throw new RuntimeException();
        }

        ArrayList<FunctionDefinitionNode> result = new ArrayList<FunctionDefinitionNode>();
        for (int i = 0; i < originalMethodDefinitions.size(); i++) {
            FunctionDefinitionNode oM = originalMethodDefinitions.get(i);
            FunctionDefinitionNode mM = modifiedMethodDefinitions.get(i);

            NodeVisitor visitor = NodeVisitorFactory.getVisitor(FunctionDefinitionNode.class);
            if (!visitor.compareTrees(oM, mM)) {
                result.add(mM);
            }
        }

        return result;
    }

}
