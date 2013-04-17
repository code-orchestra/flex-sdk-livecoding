package codeOrchestra.digest;

import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public interface IClassDigest {

    Set<String> getMembers();
    String getFqName();
    String getSuperClassFQName();

}
