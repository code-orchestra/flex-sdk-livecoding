package codeOrchestra.digest;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public interface IClassDigest {

    boolean isMainClass();
    IMember getInstanceMember(String name);
    List<IMember> getAllMembers();
    List<IMember> getInstanceMembers();
    List<IMember> getStaticMembers();
    String getFqName();
    String getSuperClassFQName();
    boolean canBeUsedForLiveCoding();
    boolean isAddedDuringProcessing();

}
