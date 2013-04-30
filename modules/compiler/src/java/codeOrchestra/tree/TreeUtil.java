package codeOrchestra.tree;

import codeOrchestra.digest.DigestManager;
import codeOrchestra.digest.MemberKind;
import codeOrchestra.util.StringUtils;
import flex2.compiler.CompilationUnit;
import flex2.compiler.util.MultiName;
import flex2.compiler.util.Name;
import flex2.compiler.util.QName;
import macromedia.asc.parser.*;
import macromedia.asc.util.Context;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Anton.I.Neverov
 * @author Alexander Eliseyev
 */
public class TreeUtil {

    public static Node getType(String fqName) {
        if ("void".equals(fqName)) {
            return null;
        }

        if (fqName.contains(".")) {
            String packageName = StringUtils.namespaceFromLongName(fqName);
            String className = StringUtils.shortNameFromLongName(fqName);
            return new TypeExpressionNode(new MemberExpressionNode(null, new GetExpressionNode(new QualifiedIdentifierNode(new LiteralStringNode(packageName), className, -1)), -1), true, false);
        } else {
            return new TypeExpressionNode(TreeUtil.createIdentifier(fqName), true, false);
        }
    }

    public static MemberKind getMemberKind(DefinitionNode definitionNode) {
        if (definitionNode instanceof VariableDefinitionNode) {
            return MemberKind.FIELD;
        } else if (definitionNode instanceof FunctionDefinitionNode) {
            FunctionDefinitionNode functionDefinitionNode = (FunctionDefinitionNode) definitionNode;
            if (TreeNavigator.isGetter(functionDefinitionNode)) {
                return MemberKind.GETTER;
            } else if (TreeNavigator.isSetter(functionDefinitionNode)) {
                return MemberKind.SETTER;
            }
            return MemberKind.METHOD;
        }
        return null;
    }

    public static String getFqName(ClassDefinitionNode classDefinitionNode) {
        String packageName = classDefinitionNode.pkgdef.name.id.pkg_part;
        String className = classDefinitionNode.name.name;

        return StringUtils.longNameFromNamespaceAndShortName(packageName, className);
    }

    public static void makePublic(AttributeListNode attributeListNode, boolean skipProtected) {
        if (attributeListNode == null) {
            return;
        }
        for (Node node : attributeListNode.items) {
            if (node instanceof ListNode) {
                ListNode listNode1 = (ListNode) node;
                for (Node item1 : listNode1.items) {
                    if (item1 instanceof MemberExpressionNode) {
                        MemberExpressionNode memberExpressionNode = (MemberExpressionNode) item1;
                        if (memberExpressionNode.selector instanceof GetExpressionNode) {
                            GetExpressionNode getExpressionNode = (GetExpressionNode) memberExpressionNode.selector;
                            if (getExpressionNode.expr instanceof IdentifierNode) {
                                IdentifierNode identifierNode =  (IdentifierNode) getExpressionNode.expr;
                                if ("private".equals(identifierNode.name) || (!skipProtected && "protected".equals(identifierNode.name))) {
                                    identifierNode.name = "public";
                                }
                            }
                        }
                    }
                }
            } else if (node instanceof IdentifierNode) {
                IdentifierNode identifierNode =  (IdentifierNode) node;
                if ("private".equals(identifierNode.name) || (!skipProtected && "protected".equals(identifierNode.name))) {
                    identifierNode.name = "public";
                }
            }
        }
    }

    public static VariableDefinitionNode createLocalVariable(PackageDefinitionNode pkgdef, String name, String type, Node initializer) {
        return new VariableDefinitionNode(pkgdef, null, -112, new ListNode(null,
                new VariableBindingNode(null, null, -112, new TypedIdentifierNode(new IdentifierNode(name, -1), new TypeExpressionNode(TreeUtil.createIdentifier(type), true, false), -1), initializer),
                -1),
                -1);
    }

    public static MemberExpressionNode createPublicModifier() {
        return createIdentifier("public");
    }

    public static MemberExpressionNode createPrivateModifier() {
        return createIdentifier("private");
    }

    public static MemberExpressionNode createStaticModifier() {
        return createIdentifier("static");
    }

    public static MemberExpressionNode createDynamicModifier() {
        return createIdentifier("dynamic");
    }

    public static MemberExpressionNode createIdentifier(String name) {
        return createIdentifier(null, name);
    }

    public static MemberExpressionNode createThisIdentifier(String name) {
        MemberExpressionNode identifier = createIdentifier(null, name);
        identifier.base = new ThisExpressionNode();
        return identifier;
    }

    public static MemberExpressionNode createIdentifier(String base, String name) {
        MemberExpressionNode baseExpression = base == null ? null : createIdentifier(null, base);
        return new MemberExpressionNode(baseExpression, new GetExpressionNode(new IdentifierNode(name, -1)), -1);
    }

    public static MemberExpressionNode createCall(String base, String name, ArgumentListNode args) {
        MemberExpressionNode identifier = base == null ? null : createIdentifier(base);
        return new MemberExpressionNode(identifier, new CallExpressionNode(new IdentifierNode(name, -1), args), -1);
    }

    public static PackageNameNode createPackageNameNode(String packageName) {
        return createPackageNameNode(packageName, null);
    }

    public static PackageNameNode createPackageNameNode(String packageName, String className) {
        String[] packageNameParts = packageName.split("\\.");
        PackageIdentifiersNode packageIdentifiersNode = new PackageIdentifiersNode(
                new IdentifierNode(packageNameParts[0], -1),
                -1,
                true
        );
        for (int i = 1; i < packageNameParts.length; i++) {
            packageIdentifiersNode.list.add(new IdentifierNode(packageNameParts[i], -1));
        }
        packageIdentifiersNode.pkg_part = packageName;
        if (className != null) {
            packageIdentifiersNode.list.add(new IdentifierNode(className, -1));
            packageIdentifiersNode.def_part = className;
        }
        return new PackageNameNode(packageIdentifiersNode, -1);
    }

    public static ParameterNode createParameterNode(String paramName, String paramType) {
        return new ParameterNode(
                Tokens.VAR_TOKEN,
                new IdentifierNode(paramName, -1),
                paramType != null ? new TypeExpressionNode(createIdentifier(paramType), true, false) : null,
                null // Place initializer here if needed
        );
    }

    public static void addImport(CompilationUnit unit, String packageName, String className) {
        ClassDefinitionNode classDefinitionNode = TreeNavigator.getClassDefinition(unit);
        ImportDirectiveNode importDirectiveNode = new ImportDirectiveNode(
                classDefinitionNode.pkgdef,
                null,
                TreeUtil.createPackageNameNode(packageName, className),
                null,
                classDefinitionNode.cx
        );
        ((ProgramNode) unit.getSyntaxTree()).statements.items.add(importDirectiveNode);
        classDefinitionNode.pkgdef.statements.items.add(importDirectiveNode);
    }

    public static FunctionDefinitionNode removeAllMethodsAndClearConstructor(ClassDefinitionNode classDefinitionNode) {
        List<FunctionDefinitionNode> allMethodDefinitions = TreeNavigator.getAllMethodDefinitions(classDefinitionNode);
        Iterator<FunctionDefinitionNode> iterator = allMethodDefinitions.iterator();
        FunctionDefinitionNode constructor = null;
        while (iterator.hasNext()) {
            FunctionDefinitionNode functionDefinitionNode = iterator.next();
            if (TreeNavigator.isConstructor(functionDefinitionNode, classDefinitionNode)) {
                constructor = functionDefinitionNode;
                constructor.fexpr.body.items.clear();
            } else {
                iterator.remove();
            }
        }
        if (constructor == null) {
            MethodCONode methodCONode = new MethodCONode(classDefinitionNode.name.name, null, classDefinitionNode.cx);
            constructor = methodCONode.getFunctionDefinitionNode();
            classDefinitionNode.statements.items.add(constructor);
        }
        return constructor;
    }

    public static void createUnitFromInternalClass(ClassDefinitionNode internalClass, String packageName, Context cx, List<ImportDirectiveNode> imports, Set<Name> inheritance) {
        String className = internalClass.name.name;
        ClassCONode classCONode = new ClassCONode(packageName, className, cx);

        for (ImportDirectiveNode importDirectiveNode : imports) {
            PackageIdentifiersNode packageIdentifierNode = importDirectiveNode.name.id;
            classCONode.addImport(packageIdentifierNode.pkg_part, packageIdentifierNode.def_part);
        }

        classCONode.addToProject();

        ClassDefinitionNode newClass = classCONode.getMyClass();
        newClass.baseclass = internalClass.baseclass;
        newClass.statements = internalClass.statements;
        newClass.interfaces = internalClass.interfaces;

        for (Name inheritanceName : inheritance) {
            if (inheritanceName instanceof MultiName) {
                MultiName multiName = (MultiName) inheritanceName;

                for (int i = 0; i < multiName.getNumQNames(); i++) {
                    QName qName = multiName.getQName(i);
                    if (DigestManager.getInstance().isAvailable(qName)) {
                        classCONode.getCompilationUnit().inheritance.add(qName);
                    }
                }
            } else if (inheritanceName instanceof QName) {
                classCONode.getCompilationUnit().inheritance.add(inheritanceName);
            }
        }
    }

}
