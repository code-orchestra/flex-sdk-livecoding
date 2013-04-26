package codeOrchestra.digest.impl;

import codeOrchestra.digest.IClassDigest;
import codeOrchestra.digest.IMember;
import codeOrchestra.digest.MemberKind;
import codeOrchestra.digest.Visibility;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public class SWCClassDigest implements IClassDigest {

    private Set<IMember> instanceMembers = new HashSet<IMember>();
    private Set<IMember> staticMembers = new HashSet<IMember>();
    private Set<IMember> members = new HashSet<IMember>();

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
                    memberElement.hasAttribute("visibility") ? Visibility.valueOf(memberElement.getAttribute("visibility")) : Visibility.UNKNOWN
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
    public Set<IMember> getInstanceMembers() {
        return instanceMembers;
    }

    @Override
    public Set<IMember> getStaticMembers() {
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
}
