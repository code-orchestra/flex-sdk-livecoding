package codeOrchestra.digest.impl;

import codeOrchestra.digest.IClassDigest;
import codeOrchestra.digest.IMember;
import codeOrchestra.digest.MemberKind;
import codeOrchestra.digest.Visibility;

/**
 * @author Alexander Eliseyev
 */
public abstract class AbstractMember implements IMember {

    private String name;
    private boolean isStatic;
    private MemberKind kind;
    private Visibility visibility;
    private IClassDigest parent;

    public AbstractMember(String name, boolean isStatic, MemberKind kind, Visibility visibility, IClassDigest parent) {
        this.name = name;
        this.isStatic = isStatic;
        this.kind = kind;
        this.visibility = visibility;
        this.parent = parent;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String getName() {
        return name;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public MemberKind getKind() {
        return kind;
    }

    @Override
    public IClassDigest getParent() {
        return parent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isStatic ? 1231 : 1237);
        result = prime * result + ((kind == null) ? 0 : kind.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        IMember other = (IMember) obj;
        if (isStatic != other.isStatic())
            return false;
        if (kind != other.getKind())
            return false;
        if (name == null) {
            if (other.getName() != null)
                return false;
        } else if (!name.equals(other.getName()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", isStatic=" + isStatic +
                ", kind=" + kind +
                ", visibility=" + visibility +
                ", parent=" + parent +
                '}';
    }
}
