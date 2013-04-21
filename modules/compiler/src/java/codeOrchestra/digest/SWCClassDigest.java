package codeOrchestra.digest;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public class SWCClassDigest implements IClassDigest {

    private Set<String> staticMembers = new HashSet<String>();
    private Set<String> members = new HashSet<String>();
    private String fqName;
    private String superClassFqName;

    public SWCClassDigest(Element traitElement) {
        fqName = traitElement.getAttribute("fqName");
        superClassFqName = traitElement.getAttribute("superFqName");

        NodeList membersList = traitElement.getElementsByTagName("member");
        for (int i = 0; i < membersList.getLength(); i++) {
            Element memberElement = (Element) membersList.item(i);
            String memberName = memberElement.getAttribute("name");

            members.add(memberName);
            if (memberElement.hasAttribute("static")) {
                staticMembers.add(memberName);
            }
        }
    }

    @Override
    public Set<String> getMembers() {
        return members;
    }

    @Override
    public Set<String> getStaticMembers() {
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
