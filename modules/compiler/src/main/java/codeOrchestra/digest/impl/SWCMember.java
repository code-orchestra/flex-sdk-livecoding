package codeOrchestra.digest.impl;

import codeOrchestra.digest.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class SWCMember extends AbstractMember implements IMember {

    private List<IParameter> parameters;

    private String typeFqName;

    public SWCMember(String name, String typeFqName, boolean isStatic, MemberKind kind, Visibility visibility, IClassDigest parent) {
        super(name, isStatic, kind, visibility, parent);
        this.typeFqName = typeFqName;
    }

    @Override
    public String getType() {
        if ("__AS3__.vec.Vector".equals(typeFqName) || "Vector".equals(typeFqName)) {
            return null;
        }
        return typeFqName;
    }

    @Override
    public List<IParameter> getParameters() {
        if (parameters == null) {
            return Collections.emptyList();
        }
        return parameters;
    }

    @Override
    public boolean isAddedDuringProcessing() {
        return false;
    }

    public void addParameter(String name, String type) {
        if (parameters == null) {
            parameters = new ArrayList<IParameter>();
        }
        parameters.add(new SWCParameter(name, type));
    }

}
