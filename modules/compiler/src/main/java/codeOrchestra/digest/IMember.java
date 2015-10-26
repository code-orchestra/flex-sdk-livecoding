package codeOrchestra.digest;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public interface IMember {

    Visibility getVisibility();
    String getType();
    String getName();
    boolean isStatic();
    MemberKind getKind();
    List<IParameter> getParameters();
    IClassDigest getParent();
    boolean isAddedDuringProcessing();

}
