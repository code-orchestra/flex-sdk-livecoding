package codeOrchestra.digest;

import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public interface IClassDigest {

    Set<String> getMembers();
    Set<String> getInstanceMembers();
    Set<String> getStaticMembers();
    String getFqName();
    String getSuperClassFQName();

}
