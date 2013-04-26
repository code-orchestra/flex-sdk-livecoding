package codeOrchestra.digest;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public interface IClassDigest {

    IMember getInstanceMember(String name);
    List<IMember> getInstanceMembers();
    List<IMember> getStaticMembers();
    String getFqName();
    String getSuperClassFQName();

}
