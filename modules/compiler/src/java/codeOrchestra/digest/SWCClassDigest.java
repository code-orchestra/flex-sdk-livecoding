package codeOrchestra.digest;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public class SWCClassDigest implements IClassDigest {

    private Set<Member> instanceMembers = new HashSet<Member>();
    private Set<Member> staticMembers = new HashSet<Member>();
    private Set<Member> members = new HashSet<Member>();

    private String fqName;
    private String superClassFqName;

    public SWCClassDigest(Element traitElement) {
        fqName = traitElement.getAttribute("fqName");
        superClassFqName = traitElement.getAttribute("superFqName");

        NodeList membersList = traitElement.getElementsByTagName("member");
        for (int i = 0; i < membersList.getLength(); i++) {
            Element memberElement = (Element) membersList.item(i);

            Member member = new Member(
                    memberElement.getAttribute("name"),
                    memberElement.hasAttribute("static"),
                    MemberKind.valueOf(memberElement.getAttribute("kind"))
            );

            members.add(member);
            if (member.isStatic()) {
                staticMembers.add(member);
            } else {
                instanceMembers.add(member);
            }
        }
    }

    @Override
    public Set<Member> getMembers() {
        return members;
    }

    @Override
    public Set<Member> getInstanceMembers() {
        return instanceMembers;
    }

    @Override
    public Set<Member> getStaticMembers() {
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
