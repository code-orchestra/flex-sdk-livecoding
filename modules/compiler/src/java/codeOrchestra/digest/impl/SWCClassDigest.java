package codeOrchestra.digest.impl;

import codeOrchestra.digest.IClassDigest;
import codeOrchestra.digest.IMember;
import codeOrchestra.digest.MemberKind;
import codeOrchestra.digest.Visibility;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public class SWCClassDigest implements IClassDigest {

    private List<IMember> instanceMembers = new ArrayList<IMember>();
    private List<IMember> staticMembers = new ArrayList<IMember>();
    private List<IMember> members = new ArrayList<IMember>();

    private String fqName;
    private String superClassFqName;

    public SWCClassDigest(Element traitElement) {
        fqName = traitElement.getAttribute("fqName");
        superClassFqName = traitElement.getAttribute("superFqName");

        NodeList membersList = traitElement.getElementsByTagName("member");
        for (int i = 0; i < membersList.getLength(); i++) {
            Element memberElement = (Element) membersList.item(i);

            SWCMember member = new SWCMember(
                    memberElement.getAttribute("name"),
                    memberElement.getAttribute("type"),
                    memberElement.hasAttribute("static"),
                    MemberKind.valueOf(memberElement.getAttribute("kind")),
                    memberElement.hasAttribute("visibility") ? Visibility.valueOf(memberElement.getAttribute("visibility")) : Visibility.UNKNOWN,
                    this
            );
            NodeList parameters = memberElement.getElementsByTagName("parameter");
            for (int j = 0; j < parameters.getLength(); j++) {
                Element parameterElement = (Element) parameters.item(j);
                member.addParameter(parameterElement.getAttribute("name"), parameterElement.getAttribute("type"));
            }

            members.add(member);
            if (member.isStatic()) {
                staticMembers.add(member);
            } else {
                instanceMembers.add(member);
            }
        }
    }

    @Override
    public IMember getInstanceMember(String name) {
        for (IMember instanceMember : instanceMembers) {
            if (name.equals(instanceMember.getName())) {
                return instanceMember;
            }
        }
        return null;
    }

    @Override
    public List<IMember> getAllMembers() {
        List<IMember> allMembers = new ArrayList<IMember>();
        allMembers.addAll(getInstanceMembers());
        allMembers.addAll(getStaticMembers());
        return allMembers;
    }

    @Override
    public List<IMember> getInstanceMembers() {
        return instanceMembers;
    }

    @Override
    public List<IMember> getStaticMembers() {
        return staticMembers;
    }

    @Override
    public String getFqName() {
        return fqName;
    }

    @Override
    public String getSuperClassFQName() {
        return superClassFqName;
    }

    @Override
    public boolean canBeUsedForLiveCoding() {
        return false;
    }
}
