package codeOrchestra.digest.impl;

import codeOrchestra.digest.IParameter;
import codeOrchestra.digest.ITypeResolver;

/**
 * @author Alexander Eliseyev
 */
public class SourceParameter implements IParameter {

    private String typeFqName;

    private transient String shortTypeName;
    private String name;

    private boolean resolved;

    public SourceParameter(String shortTypeName, String name) {
        this.shortTypeName = shortTypeName;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        if (!resolved) {
            throw new IllegalStateException("Unresolved");
        }
        return typeFqName;
    }

    public void resolve(ITypeResolver typeResolver) {
        typeFqName = typeResolver.tryResolve(shortTypeName);
        resolved = true;
    }

}
