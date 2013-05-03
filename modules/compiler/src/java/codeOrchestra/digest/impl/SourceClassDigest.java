package codeOrchestra.digest.impl;

import codeOrchestra.LiveCodingUtil;
import codeOrchestra.digest.*;
import codeOrchestra.tree.TreeNavigator;
import codeOrchestra.util.StringUtils;
import macromedia.asc.parser.*;

import java.util.*;

/**
 * @author Alexander Eliseyev
 */
public class SourceClassDigest implements IClassDigest, ITypeResolver {

    private String name;
    private String packageName;
    private String superClassShortName;

    private List<String> asterixImports = new ArrayList<String>();
    // Short name -> FQ name
    private Map<String, String> importMap = new HashMap<String, String>();

    private String superClassFQName;

    private List<IMember> members = new ArrayList<IMember>();
    private List<IMember> staticMembers = new ArrayList<IMember>();
    private List<IMember> instanceMembers = new ArrayList<IMember>();

    private boolean live;

    public SourceClassDigest(ClassDefinitionNode cl) {
        // Name
        packageName = cl.pkgdef.name.id.pkg_part;
        name = cl.name.name;

        // Superclass short name
        // TODO: definition by fq-name?
        if (cl.baseclass != null && cl.baseclass instanceof MemberExpressionNode) {
            MemberExpressionNode memberExpressionNode = (MemberExpressionNode) cl.baseclass;
            SelectorNode selector = memberExpressionNode.selector;

            if (selector.expr != null && selector.expr instanceof IdentifierNode) {
                IdentifierNode identifierNode = (IdentifierNode) selector.expr;
                superClassShortName = identifierNode.name;
            }
        }

        // Imports
        List<ImportDirectiveNode> imports = TreeNavigator.getImports(cl.pkgdef);
        for (ImportDirectiveNode importDirectiveNode : imports) {
            PackageIdentifiersNode packageIdentifiersNode = importDirectiveNode.name.id;
            String importedPackage = packageIdentifiersNode.pkg_part;
            String importedShortName = packageIdentifiersNode.def_part;

            if (StringUtils.isEmpty(importedShortName)) {
                asterixImports.add(importedPackage);
            } else {
                importMap.put(importedShortName, StringUtils.longNameFromNamespaceAndShortName(importedPackage, importedShortName));
            }
        }

        // Members
        // Fields
        for (VariableDefinitionNode fieldDefinition : TreeNavigator.getFieldDefinitions(cl)) {
            for (Node item : fieldDefinition.list.items) {
                if (item instanceof VariableBindingNode) {
                    VariableBindingNode variableBindingNode = (VariableBindingNode) item;
                    String fieldName = variableBindingNode.variable.identifier.name;

                    String typeShortName = getShortTypeName(variableBindingNode.variable.type);
                    SourceMember member = new SourceMember(fieldName, typeShortName, TreeNavigator.isStaticField(variableBindingNode), MemberKind.FIELD, TreeNavigator.getVisibility(fieldDefinition), this);

                    members.add(member);
                    if (member.isStatic()) {
                        staticMembers.add(member);
                    } else {
                        instanceMembers.add(member);
                    }
                }
            }
        }
        // Methods
        for (FunctionDefinitionNode functionDefinitionNode : TreeNavigator.getMethodDefinitions(cl)) {
            String methodName = functionDefinitionNode.name.identifier.name;
            MemberKind memberKind = TreeNavigator.getMemberKind(functionDefinitionNode);
            String typeShortName = getShortTypeName(functionDefinitionNode.fexpr.signature.result);

            SourceMember member = new SourceMember(methodName, typeShortName, TreeNavigator.isStaticMethod(functionDefinitionNode), memberKind, TreeNavigator.getVisibility(functionDefinitionNode), this);
            ParameterListNode parameters = functionDefinitionNode.fexpr.signature.parameter;
            if (parameters != null) {
                for (ParameterNode parameterNode : parameters.items) {
                    member.addParameter(parameterNode.identifier.name, getShortTypeName(parameterNode.type));
                }
            }

            members.add(member);
            if (member.isStatic()) {
                staticMembers.add(member);
            } else {
                instanceMembers.add(member);
            }
        }

        this.live = LiveCodingUtil.canBeUsedForLiveCoding(cl);
    }

    private String getShortTypeName(Node typeNode) {
        if (typeNode == null) {
            return "void";
        }
        if (typeNode instanceof TypeExpressionNode) {
            TypeExpressionNode typeExpressionNode = (TypeExpressionNode) typeNode;
            Node typeNodeExpression = typeExpressionNode.expr;
            if (typeNodeExpression instanceof IdentifierNode) {
                return ((IdentifierNode) typeNodeExpression).name;
            } else if (typeNodeExpression instanceof MemberExpressionNode) {
                return ((MemberExpressionNode) typeNodeExpression).selector.getIdentifier().name;
            } else {
                System.err.println("*** Warning: Unsupported type expression node: " + typeNodeExpression.getClass().getSimpleName());
            }
        } else {
            System.err.println("*** Warning: Unsupported type node: " + typeNode.getClass().getSimpleName());
        }
        return null;
    }

    @Override
    public IMember getInstanceMember(String name) {
        for (IMember instanceMember : instanceMembers) {
            if (name.equals(instanceMember.getName())) {
                return instanceMember;
            }
        }
        return null;
    }

    public List<IMember> getInstanceMembers() {
        return instanceMembers;
    }

    public List<IMember> getStaticMembers() {
        return staticMembers;
    }

    public String getSuperClassFQName() {
        return superClassFQName;
    }

    public void resolve() {
        this.superClassFQName = tryResolve(superClassShortName);
        for (IMember member : members) {
            ((SourceMember) member).resolve(this);
        }
    }

    public String tryResolve(String shortClassName) {
        if (shortClassName == null) {
            return null;
        }

        if ("*".equals(shortClassName) || "void".equals(shortClassName)) {
            return shortClassName;
        }

        // Try explicit imports
        String fqNameCandidate = importMap.get(shortClassName);
        if (fqNameCandidate != null) {
            return fqNameCandidate;
        }

        // Try asterix imports
        for (String asterixPackage : asterixImports) {
            fqNameCandidate = tryResolve(asterixPackage, shortClassName);
            if (fqNameCandidate != null) {
                return fqNameCandidate;
            }
        }

        // Try same package
        fqNameCandidate = tryResolve(packageName, shortClassName);
        if (fqNameCandidate != null) {
            return fqNameCandidate;
        }

        // Try default package
        fqNameCandidate = tryResolve("", shortClassName);
        if (fqNameCandidate != null) {
            return fqNameCandidate;
        }

        return shortClassName;
    }

    /**
     * @return fq name if resolved, null otherwise
     */
    private String tryResolve(String packageName, String className) {
        String fqName = StringUtils.longNameFromNamespaceAndShortName(packageName, className);
        if (DigestManager.getInstance().isAvailable(fqName)) {
            return fqName;
        }
        return null;
    }

    public String getFqName() {
        return StringUtils.longNameFromNamespaceAndShortName(packageName, name);
    }

    @Override
    public boolean canBeUsedForLiveCoding() {
        return live;
    }
}
