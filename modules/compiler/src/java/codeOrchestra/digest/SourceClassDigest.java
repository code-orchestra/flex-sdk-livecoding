package codeOrchestra.digest;

import codeOrchestra.tree.TreeNavigator;
import codeOrchestra.util.StringUtils;
import macromedia.asc.parser.*;

import java.util.*;

/**
 * @author Alexander Eliseyev
 */
public class SourceClassDigest implements IClassDigest {

    private String name;
    private String packageName;
    private String superClassShortName;

    private List<String> asterixImports = new ArrayList<String>();
    // Short name -> FQ name
    private Map<String, String> importMap = new HashMap<String, String>();

    private String superClassFQName;

    private Set<Member> members = new HashSet<Member>();
    private Set<Member> staticMembers = new HashSet<Member>();
    private Set<Member> instanceMembers = new HashSet<Member>();

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

                    Member member = new Member(fieldName, TreeNavigator.isStaticField(variableBindingNode), MemberKind.FIELD, TreeNavigator.getVisibility(fieldDefinition));
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
            MemberKind memberKind = MemberKind.METHOD;
            if (TreeNavigator.isGetter(functionDefinitionNode)) {
                memberKind = MemberKind.GETTER;
            } else if (TreeNavigator.isSetter(functionDefinitionNode)) {
                memberKind = MemberKind.SETTER;
            }

            Member member = new Member(methodName, TreeNavigator.isStaticMethod(functionDefinitionNode), memberKind, TreeNavigator.getVisibility(functionDefinitionNode));
            members.add(member);
            if (member.isStatic()) {
                staticMembers.add(member);
            } else {
                instanceMembers.add(member);
            }
        }
    }

    @Override
    public Member getInstanceMember(String name) {
        for (Member instanceMember : instanceMembers) {
            if (name.equals(instanceMember.getName())) {
                return instanceMember;
            }
        }
        return null;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public Set<Member> getInstanceMembers() {
        return instanceMembers;
    }

    public Set<Member> getStaticMembers() {
        return staticMembers;
    }

    public String getSuperClassFQName() {
        return superClassFQName;
    }

    public void resolve() {
        if (superClassShortName == null) {
            return;
        }

        // Try explicit imports
        String fqNameCandidate = importMap.get(superClassShortName);
        if (fqNameCandidate != null) {
            superClassFQName = fqNameCandidate;
            return;
        }

        // Try asterix imports
        for (String asterixPackage : asterixImports) {
            fqNameCandidate = tryResolve(asterixPackage, superClassShortName);
            if (fqNameCandidate != null) {
                superClassFQName = fqNameCandidate;
                return;
            }
        }

        // Try same package
        fqNameCandidate = tryResolve(packageName, superClassShortName);
        if (fqNameCandidate != null) {
            superClassFQName = fqNameCandidate;
            return;
        }

        // Try default package
        fqNameCandidate = tryResolve("", superClassShortName);
        if (fqNameCandidate != null) {
            superClassFQName = fqNameCandidate;
            return;
        }
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

}
