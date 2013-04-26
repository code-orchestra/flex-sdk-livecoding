package codeOrchestra.digest;

import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public interface IClassDigest {

    IMember getInstanceMember(String name);
    Set<IMember> getInstanceMembers();
    Set<IMember> getStaticMembers();
    String getFqName();
    String getSuperClassFQName();

}
