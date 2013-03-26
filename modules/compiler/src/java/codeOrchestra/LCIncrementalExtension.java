package codeOrchestra;

import codeOrchestra.tree.TreeNavigator;
import codeOrchestra.tree.TreeUtil;
import codeOrchestra.tree.visitor.NodeVisitor;
import codeOrchestra.tree.visitor.NodeVisitorFactory;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.*;
import macromedia.asc.util.ObjectList;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LCIncrementalExtension extends AbstractTreeModificationExtension {

    private final String initialClassName;
    private final String initialPackageName;

    public LCIncrementalExtension(String fqClassName) {
        String[] parts = fqClassName.split(":");
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

        FunctionDefinitionNode changedMethod = findChangedMethod(originalClass, classDefinitionNode);
        System.out.println("Comparison of files took " + (System.currentTimeMillis() - l) + "ms");

        if (changedMethod != null) {
            ObjectList<Node> oldBody = changedMethod.fexpr.body.items;
            changedMethod.fexpr.body.items = new ObjectList<Node>();
            String liveCodingClassName = addLiveCodingClass(className, changedMethod, oldBody, true);

            FunctionDefinitionNode constructor = TreeUtil.removeAllMethodsAndClearConstructor(classDefinitionNode);
            constructor.fexpr.body.items.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier(liveCodingClassName), -1)));
            TreeUtil.addImport(unit, "codeOrchestra.liveCoding.load", liveCodingClassName);
        }
    }

    /**
     * Returns first changed method
     *
     * We hope that:
     *  only one method can be changed in one incremental step
     *  method order remains unchanged
     */
    private FunctionDefinitionNode findChangedMethod(ClassDefinitionNode originalClass, ClassDefinitionNode modifiedClass) {
        List<FunctionDefinitionNode> originalMethodDefinitions = TreeNavigator.getMethodDefinitions(originalClass);
        List<FunctionDefinitionNode> modifiedMethodDefinitions = TreeNavigator.getMethodDefinitions(modifiedClass);

        if (originalMethodDefinitions.size() != modifiedMethodDefinitions.size()) {
            throw new RuntimeException();
        }

        for (int i = 0; i < originalMethodDefinitions.size(); i++) {
            FunctionDefinitionNode oM = originalMethodDefinitions.get(i);
            FunctionDefinitionNode mM = modifiedMethodDefinitions.get(i);

            NodeVisitor visitor = NodeVisitorFactory.getVisitor(FunctionDefinitionNode.class);
            if (!visitor.compareTrees(oM, mM)) {
                return mM;
            }
        }

        return null;
    }

}
