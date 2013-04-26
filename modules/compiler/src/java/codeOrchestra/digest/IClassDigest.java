package codeOrchestra.digest;

import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public interface IClassDigest {

    Member getInstanceMember(String name);
    Set<Member> getMembers();
    Set<Member> getInstanceMembers();
    Set<Member> getStaticMembers();
    String getFqName();
    String getSuperClassFQName();

}
