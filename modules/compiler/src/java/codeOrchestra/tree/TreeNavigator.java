package codeOrchestra.tree;

import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class TreeNavigator {

    public static ClassDefinitionNode getClassDefinition(CompilationUnit unit) {
        return getClassDefinition(((ProgramNode) unit.getSyntaxTree()));
    }

    public static ClassDefinitionNode getClassDefinition(ProgramNode programNode) {
        StatementListNode statements = programNode.statements;
        for (Node item : statements.items) {
            if (item instanceof ClassDefinitionNode) {
                return (ClassDefinitionNode) item;
            }
        }
        return null;
    }

    public static List<FunctionDefinitionNode> getMethodDefinitions(ClassDefinitionNode classDefinitionNode) {
        List<FunctionDefinitionNode> functionDefinitionNodes = getAllMethodDefinitions(classDefinitionNode);
        Iterator<FunctionDefinitionNode> iterator = functionDefinitionNodes.iterator();
        while (iterator.hasNext()) {
            FunctionDefinitionNode functionDefinitionNode = iterator.next();
            if (isConstructor(functionDefinitionNode, classDefinitionNode)) {
                iterator.remove();
            }
        }
        return  functionDefinitionNodes;
    }

    public static FunctionDefinitionNode getConstructorDefinition(ClassDefinitionNode classDefinitionNode) {
        for (FunctionDefinitionNode functionDefinitionNode : getAllMethodDefinitions(classDefinitionNode)) {
            if (isConstructor(functionDefinitionNode, classDefinitionNode)) {
                return functionDefinitionNode;
            }
        }
        return null;
    }

    private static List<FunctionDefinitionNode> getAllMethodDefinitions(ClassDefinitionNode classDefinitionNode) {
        List<FunctionDefinitionNode> functionDefinitionNodes = new ArrayList<FunctionDefinitionNode>();
        for (Node item : classDefinitionNode.statements.items) {
            if (item instanceof FunctionDefinitionNode) {
                functionDefinitionNodes.add((FunctionDefinitionNode) item);
            }
        }
        return  functionDefinitionNodes;
    }

    private static boolean isConstructor(FunctionDefinitionNode functionDefinitionNode, ClassDefinitionNode classDefinitionNode) {
        return functionDefinitionNode.name.identifier.name.equals(classDefinitionNode.name.name);
    }

    public static List<ImportDirectiveNode> getImports(PackageDefinitionNode packageDefinitionNode) {
        List<ImportDirectiveNode> importDirectiveNodes = new ArrayList<ImportDirectiveNode>();
        for (Node item : packageDefinitionNode.statements.items) {
            if (item instanceof ImportDirectiveNode) {
                importDirectiveNodes.add((ImportDirectiveNode) item);
            }
        }
        return importDirectiveNodes;
    }

    public static boolean isStaticMethod(FunctionDefinitionNode functionDefinitionNode) {
        return methodHasAttribute(functionDefinitionNode, "static");
    }

    private static boolean methodHasAttribute(FunctionDefinitionNode functionDefinitionNode, String attrName) {
        for (Node item : functionDefinitionNode.attrs.items) {
            Node node;
            if (item instanceof ListNode) {
                node = ((ListNode) item).items.at(0);
            } else if (item instanceof MemberExpressionNode) {
                node = item;
            } else {
                continue;
            }
            if (!(node instanceof MemberExpressionNode)) { continue; }
            SelectorNode selector = ((MemberExpressionNode) node).selector;
            if (!(selector instanceof GetExpressionNode)) { continue; }
            Node expr = selector.expr;
            if (!(expr instanceof IdentifierNode)) { continue; }
            if (((IdentifierNode) expr).name.equals(attrName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGetter(FunctionDefinitionNode functionDefinitionNode) {
        return functionDefinitionNode.name.kind == Tokens.GET_TOKEN;
    }

    public static boolean isSetter(FunctionDefinitionNode functionDefinitionNode) {
        return functionDefinitionNode.name.kind == Tokens.SET_TOKEN;
    }

}
