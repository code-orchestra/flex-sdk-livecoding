package codeOrchestra;

import codeOrchestra.tree.TreeNavigator;
import codeOrchestra.tree.visitor.NodeVisitor;
import codeOrchestra.tree.visitor.NodeVisitorFactory;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.*;
import macromedia.asc.util.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LCIncrementalExtension extends AbstractTreeModificationExtension {

    private final String liveCodingClassName;
    private final String liveCodingPackageName;

    public LCIncrementalExtension(String fqClassName) {
        String[] parts = fqClassName.split(":");
        liveCodingPackageName = parts[0];
        liveCodingClassName = parts[1];
    }

    @Override
    protected void performModifications(CompilationUnit unit) {
        ClassDefinitionNode modifiedClass = TreeNavigator.getClassDefinition(unit);
        if (!(liveCodingPackageName.equals(modifiedClass.pkgdef.name.id.pkg_part) && liveCodingClassName.equals(modifiedClass.name.name))) {
            return;
        }

        loadSyntaxTrees();
        saveSyntaxTree(unit);
        ProgramNode syntaxTree = projectNavigator.getSyntaxTree(modifiedClass.pkgdef.name.id.pkg_part, modifiedClass.name.name);
        ClassDefinitionNode originalClass = TreeNavigator.getClassDefinition(syntaxTree);
        FunctionDefinitionNode changedMethod = findChangedMethod(originalClass, modifiedClass);

        if (changedMethod != null) {
            processMethod(changedMethod);
        }
    }

    private void processMethod(FunctionDefinitionNode functionDefinitionNode) {
        System.out.println(functionDefinitionNode.name.identifier.name);
    }

    /*
        Returns first changed method

        We hope that:
            only one method can be changed in one incremental step
            method order remains unchanged
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
