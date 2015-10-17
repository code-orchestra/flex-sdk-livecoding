package codeOrchestra.digest.impl;

import codeOrchestra.digest.*;
import codeOrchestra.tree.TreeNavigator;
import macromedia.asc.parser.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class SourceMember extends AbstractMember implements IMember {

    public static SourceMember fromFunctionDefinition(FunctionDefinitionNode functionDefinitionNode, IClassDigest parent) {
        String methodName = functionDefinitionNode.name.identifier.name;
        MemberKind memberKind = TreeNavigator.getMemberKind(functionDefinitionNode);
        String typeShortName = TreeNavigator.getShortTypeName(functionDefinitionNode.fexpr.signature.result);

        SourceMember member = new SourceMember(methodName, typeShortName, TreeNavigator.isStaticMethod(functionDefinitionNode), memberKind, TreeNavigator.getVisibility(functionDefinitionNode), parent);
        ParameterListNode parameters = functionDefinitionNode.fexpr.signature.parameter;
        if (parameters != null) {
            for (ParameterNode parameterNode : parameters.items) {
                member.addParameter(parameterNode.identifier.name, TreeNavigator.getShortTypeName(parameterNode.type));
            }
        }
        return member;
    }

    public static SourceMember fromVariableBinding(VariableBindingNode variableBindingNode, VariableDefinitionNode fieldDefinition, IClassDigest parent) {
        String fieldName = variableBindingNode.variable.identifier.name;
        String typeShortName = TreeNavigator.getShortTypeName(variableBindingNode.variable.type);

        return new SourceMember(fieldName, typeShortName, TreeNavigator.isStaticField(variableBindingNode), MemberKind.FIELD, TreeNavigator.getVisibility(fieldDefinition), parent);
    }

    private List<IParameter> parameters = new ArrayList<IParameter>();
    private String typeFqName;

    private boolean addedDuringProcessing;
    private boolean resolved;
    private transient final String shortTypeName;

    private SourceMember(String name, String shortTypeName, boolean isStatic, MemberKind kind, Visibility visibility, IClassDigest parent) {
        super(name, isStatic, kind, visibility, parent);
        this.shortTypeName = shortTypeName;
    }

    public void addParameter(String name, String shortTypeName) {
        parameters.add(new SourceParameter(shortTypeName, name));
    }

    @Override
    public String getType() {
        if (!resolved) {
            throw new IllegalStateException("Unresolved");
        }
        if ("__AS3__.vec.Vector".equals(typeFqName)) {
            return null;
        }
        return typeFqName;
    }

    @Override
    public List<IParameter> getParameters() {
        return parameters;
    }

    @Override
    public boolean isAddedDuringProcessing() {
        return addedDuringProcessing;
    }

    public void setAddedDuringProcessing(boolean addedDuringProcessing) {
        this.addedDuringProcessing = addedDuringProcessing;
    }

    public void resolve(ITypeResolver typeResolver) {
        typeFqName = typeResolver.tryResolve(shortTypeName);
        for (IParameter parameter : parameters) {
            ((SourceParameter) parameter).resolve(typeResolver);
        }
        resolved = true;
    }

}
