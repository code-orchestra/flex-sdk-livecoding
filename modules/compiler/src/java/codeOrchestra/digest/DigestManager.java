package codeOrchestra.digest;

import codeOrchestra.AbstractTreeModificationExtension;
import codeOrchestra.LiveCodingCLIParameters;
import codeOrchestra.digest.impl.SWCClassDigest;
import codeOrchestra.digest.impl.SourceClassDigest;
import codeOrchestra.tree.LastASTHolder;
import codeOrchestra.tree.TreeNavigator;
import codeOrchestra.util.FileUtils;
import codeOrchestra.util.StringUtils;
import codeOrchestra.util.XMLUtils;
import flex2.compiler.util.QName;
import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.parser.FunctionDefinitionNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.*;

/**
 * @author Alexander Eliseyev
 */
public class DigestManager {

    private static final String DIGEST_EXTENSION = ".digest";

    private static DigestManager instance = new DigestManager();
    public static DigestManager getInstance() {
        return instance;
    }

    // package name -> set of short names
    private Map<String, Set<String>> compiledClasses = new HashMap<String, Set<String>>();

    private Set<String> availableFqNames = new HashSet<String>();

    // namespace name -> URI  map
    private Map<String, String> namespaceToURI = new HashMap<String, String>();

    private Map<String, IClassDigest> digestsMap = new HashMap<String, IClassDigest>();
    private Map<String, SourceClassDigest> unresolvedDigests = new HashMap<String, SourceClassDigest>();

    public Set<IMember> getVisibleInstanceProtectedMembers(String classFqName) {
        Set<IMember> result = new HashSet<IMember>();

        IClassDigest classDigest = digestsMap.get(classFqName);
        while (classDigest != null) {
            for (IMember member : classDigest.getInstanceMembers()) {
                if (member.getVisibility() == Visibility.PROTECTED && !result.contains(member)) {
                    result.add(member);
                }
            }

            if (classDigest.getSuperClassFQName() != null) {
                classDigest = digestsMap.get(classDigest.getSuperClassFQName());
            } else {
                classDigest = null;
            }
        }

        return result;
    }

    public void addNamespace(String name, String uri) {
        namespaceToURI.put(name, uri);
    }

    public int getInheritanceLevel(String fqName) {
        int level = 0;

        IClassDigest classDigest = digestsMap.get(fqName);
        while (classDigest != null) {
            if (classDigest.getSuperClassFQName() != null) {
                classDigest = digestsMap.get(classDigest.getSuperClassFQName());
                level++;
            } else {
                classDigest = null;
            }
        }

        return level;
    }

    public boolean isAvailable(String fqName) {
        return availableFqNames.contains(fqName);
    }

    public boolean isAvailable(QName qName) {
        return isAvailable(qName.toString().replace(":", "."));
    }

    public boolean isOverriden(FunctionDefinitionNode functionDefinitionNode, String fqName) {
        String functionName = functionDefinitionNode.fexpr.identifier.name;
        MemberKind memberKind = TreeNavigator.getMemberKind(functionDefinitionNode);

        for (IClassDigest descendant : getDescendants(digestsMap.get(fqName))) {
            for (IMember member : descendant.getInstanceMembers()) {
                if (functionName.equals(member.getName()) && member.getKind() == memberKind) {
                    if (EnumSet.of(Visibility.PROTECTED, Visibility.PUBLIC).contains(member.getVisibility()) || member.getVisibility() == Visibility.PRIVATE && descendant.canBeUsedForLiveCoding()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public Iterable<IClassDigest> getDescendants(final IClassDigest root) {
        return new Iterable<IClassDigest>() {
            @Override
            public Iterator<IClassDigest> iterator() {
                return new Iterator<IClassDigest>() {
                    {
                        directDescendants = new ArrayList<IClassDigest>();
                        setRoot(root);
                    }

                    private ArrayList<IClassDigest> directDescendants;
                    private Iterator<IClassDigest> directDescendantsIterator;

                    private void setRoot(IClassDigest currentRoot) {
                        directDescendants.clear();
                        for (IClassDigest classDigest : digestsMap.values()) {
                            if (classDigest instanceof SourceClassDigest && currentRoot.getFqName().equals(classDigest.getSuperClassFQName())) {
                                directDescendants.add(classDigest);
                            }
                        }

                        directDescendantsIterator = directDescendants.iterator();
                    }

                    @Override
                    public boolean hasNext() {
                        boolean currentIteratorHasNext = directDescendantsIterator.hasNext();

                        if (!currentIteratorHasNext && !directDescendants.isEmpty()) {
                            ArrayList<IClassDigest> savedDirectDescendants = new ArrayList<IClassDigest>(directDescendants);
                            for (IClassDigest newRoot : savedDirectDescendants) {
                                setRoot(newRoot);
                                return hasNext();
                            }
                        }

                        return currentIteratorHasNext;
                    }

                    @Override
                    public IClassDigest next() {
                        return directDescendantsIterator.next();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * @return FQ name of the static member owner
     */
    public String findOwnerOfStaticMember(String classFqName, String memberName) {
        IClassDigest classDigest = digestsMap.get(classFqName);
        while (classDigest != null) {
            for (IMember member : classDigest.getStaticMembers()) {
                if (member.getName().equals(memberName)) {
                    return classDigest.getFqName();
                }
            }

            if (classDigest.getSuperClassFQName() != null) {
                classDigest = digestsMap.get(classDigest.getSuperClassFQName());
            } else {
                classDigest = null;
            }
        }

        return null;
    }

    public IClassDigest findVisibleOwnerOfInstanceMember(String classFqName, String memberName) {
        IClassDigest classDigest = digestsMap.get(classFqName);
        while (classDigest != null) {
            for (IMember member : classDigest.getInstanceMembers()) {
                if (member.getName().equals(memberName)) {
                    return classDigest;
                }
            }

            if (classDigest.getSuperClassFQName() != null) {
                classDigest = digestsMap.get(classDigest.getSuperClassFQName());
            } else {
                classDigest = null;
            }

        }

        return null;
    }

    public boolean isInstanceMemberVisibleInsideClass(String classFqName, String memberName) {
        IClassDigest classDigest = digestsMap.get(classFqName);
        while (classDigest != null) {
            for (IMember member : classDigest.getInstanceMembers()) {
                if (member.getName().equals(memberName)) {
                    return true;
                }
            }

            if (classDigest.getSuperClassFQName() != null) {
                classDigest = digestsMap.get(classDigest.getSuperClassFQName());
            } else {
                classDigest = null;
            }
        }

        return false;
    }

    public Set<String> getShortNamesFromPackage(String pack) {
        return compiledClasses.get(pack);
    }

    public void init() {
        availableFqNames.clear();

        // 1 - Init available fq names of source files
        for (String fqName : LastASTHolder.getInstance().getAvailableFqNames()) {
            String shortName = StringUtils.shortNameFromLongName(fqName);
            String packageName = StringUtils.namespaceFromLongName(fqName);

            Set<String> packageShortNames = compiledClasses.get(packageName);
            if (packageShortNames == null) {
                packageShortNames = new HashSet<String>();
                compiledClasses.put(packageName, packageShortNames);
            }
            packageShortNames.add(shortName);

            availableFqNames.add(fqName);
        }

        // 2 - Load digests and fq names from SWCs
        File digestsDir = getSWCDigestsFolder();
        List<File> digestFiles = FileUtils.listFileRecursively(digestsDir, new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(DIGEST_EXTENSION);
            }
        });
        for (File digestFile : digestFiles) {
            Document document = XMLUtils.fileToDOM(digestFile);
            NodeList traitsList = document.getDocumentElement().getElementsByTagName("trait");

            for (int i = 0; i < traitsList.getLength(); i++) {
                Element traitElement = (Element) traitsList.item(i);

                IClassDigest classDigest = new SWCClassDigest(traitElement);
                String fqName = classDigest.getFqName();
                digestsMap.put(fqName, classDigest);
                availableFqNames.add(fqName);
            }

            NodeList namespacesList = document.getDocumentElement().getElementsByTagName("namespace");
            for (int i = 0; i < namespacesList.getLength(); i++) {
                Element namespaceElement = (Element) namespacesList.item(i);

                namespaceToURI.put(namespaceElement.getAttribute("name"), namespaceElement.getAttribute("uri"));
            }
        }
    }

    public String getNamespaceURI(String namespace) {
        return namespaceToURI.get(namespace);
    }

    private File getSWCDigestsFolder() {
        return new File(LiveCodingCLIParameters.getDigestsFolder());
    }

    public void addToDigestUnresolved(ClassDefinitionNode classDefinitionNode) {
        if (classDefinitionNode == null) {
            return;
        }

        SourceClassDigest classDigest = new SourceClassDigest(classDefinitionNode);
        unresolvedDigests.put(classDigest.getFqName(), classDigest);
    }

    public void resolve() {
        for (SourceClassDigest classDigest : unresolvedDigests.values()) {
            classDigest.resolve();
            digestsMap.put(classDigest.getFqName(), classDigest);
        }
        unresolvedDigests.clear();
    }

    public IClassDigest getClassDigest(String fqName) {
        return digestsMap.get(fqName);
    }
}
