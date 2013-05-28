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

    private boolean addedDuringProcessing;

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
                    IMember member = SourceMember.fromVariableBinding((VariableBindingNode) item, fieldDefinition, this);

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
            IMember member = SourceMember.fromFunctionDefinition(functionDefinitionNode, this);

            members.add(member);
            if (member.isStatic()) {
                staticMembers.add(member);
            } else {
                instanceMembers.add(member);
            }
        }

        this.live = LiveCodingUtil.getLiveCodingPolicy(cl).isEnabled();
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

    @Override
    public List<IMember> getAllMembers() {
        List<IMember> allMembers = new ArrayList<IMember>();
        allMembers.addAll(getInstanceMembers());
        allMembers.addAll(getStaticMembers());
        return allMembers;
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

    @Override
    public boolean isAddedDuringProcessing() {
        return addedDuringProcessing;
    }

    public void setAddedDuringProcessing(boolean addedDuringProcessing) {
        this.addedDuringProcessing = addedDuringProcessing;
    }
}
