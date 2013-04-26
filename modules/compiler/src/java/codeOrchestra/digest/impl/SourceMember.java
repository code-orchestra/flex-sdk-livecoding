package codeOrchestra.digest.impl;

import codeOrchestra.digest.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class SourceMember extends AbstractMember implements IMember {

    private List<IParameter> parameters = new ArrayList<IParameter>();
    private String typeFqName;

    private boolean resolved;
    private transient final String shortTypeName;

    public SourceMember(String name, String shortTypeName, boolean isStatic, MemberKind kind, Visibility visibility) {
        super(name, isStatic, kind, visibility);
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
        return typeFqName;
    }

    @Override
    public List<IParameter> getParameters() {
        return parameters;
    }

    public void resolve(ITypeResolver typeResolver) {
        typeFqName = typeResolver.tryResolve(shortTypeName);
        for (IParameter parameter : parameters) {
            ((SourceParameter) parameter).resolve(typeResolver);
        }
        resolved = true;
    }

}
