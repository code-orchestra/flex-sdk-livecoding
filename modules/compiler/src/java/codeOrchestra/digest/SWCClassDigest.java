package codeOrchestra.digest;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public class SWCClassDigest implements IClassDigest {

    private Set<String> members = new HashSet<String>();
    private String fqName;
    private String superClassFqName;

    public SWCClassDigest(Element traitElement) {
        fqName = traitElement.getAttribute("fqName");
        superClassFqName = traitElement.getAttribute("superFqName");

        NodeList membersList = traitElement.getElementsByTagName("member");
        for (int i = 0; i < membersList.getLength(); i++) {
            Element memberElement = (Element) membersList.item(i);
            members.add(memberElement.getAttribute("name"));
        }
    }

    @Override
    public Set<String> getMembers() {
        return members;
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
