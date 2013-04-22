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

    private Set<String> members = new HashSet<String>();
    private Set<String> staticMembers = new HashSet<String>();
    private Set<String> instanceMembers = new HashSet<String>();

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

                    members.add(fieldName);
                    if (TreeNavigator.isStaticField(variableBindingNode)) {
                        staticMembers.add(fieldName);
                    } else {
                        instanceMembers.add(fieldName);
                    }
                }
            }
        }
        // Methods
        for (FunctionDefinitionNode functionDefinitionNode : TreeNavigator.getMethodDefinitions(cl)) {
            String methodName = functionDefinitionNode.name.identifier.name;

            members.add(methodName);
            if (TreeNavigator.isStaticMethod(functionDefinitionNode)) {
                staticMembers.add(methodName);
            } else {
                instanceMembers.add(methodName);
            }
        }
    }

    public Set<String> getMembers() {
        return members;
    }

    public Set<String> getInstanceMembers() {
        return instanceMembers;
    }

    public Set<String> getStaticMembers() {
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
