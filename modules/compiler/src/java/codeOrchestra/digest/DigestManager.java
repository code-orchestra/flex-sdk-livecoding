package codeOrchestra.digest;

import codeOrchestra.AbstractTreeModificationExtension;
import codeOrchestra.LiveCodingCLIParameters;
import codeOrchestra.util.FileUtils;
import codeOrchestra.util.XMLUtils;
import macromedia.asc.parser.ClassDefinitionNode;
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

    private Set<String> availableFqNames = new HashSet<String>();

    private Map<String, IClassDigest> digestsMap = new HashMap<String, IClassDigest>();
    private Map<String, SourceClassDigest> unresolvedDigests = new HashMap<String, SourceClassDigest>();

    public boolean isAvailable(String fqName) {
        return availableFqNames.contains(fqName);
    }

    public boolean isMemberVisibleInsideClass(String classFqName, String memberName) {
        IClassDigest classDigest = digestsMap.get(classFqName);
        while (classDigest != null) {
            if (classDigest.getMembers().contains(memberName)) {
                return true;
            }

            if (classDigest.getSuperClassFQName() != null) {
                classDigest = digestsMap.get(classDigest.getSuperClassFQName());
            }
        }

        return false;
    }

    public void init() {
        availableFqNames.clear();

        // 1 - Init available fq names of source files
        File dir = new File(AbstractTreeModificationExtension.getCachesDir());
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(AbstractTreeModificationExtension.SERIALIZED_AST);
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            availableFqNames.add(fileName.substring(0, fileName.length() - AbstractTreeModificationExtension.SERIALIZED_AST.length()));
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
        }
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
}
