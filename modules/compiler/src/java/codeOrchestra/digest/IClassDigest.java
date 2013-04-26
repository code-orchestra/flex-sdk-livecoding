package codeOrchestra.digest;

import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public interface IClassDigest {

    Set<Member> getMembers();
    Set<Member> getInstanceMembers();
    Set<Member> getStaticMembers();
    String getFqName();
    String getSuperClassFQName();

}
