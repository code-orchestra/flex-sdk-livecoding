package codeOrchestra.digest;

import codeOrchestra.AbstractTreeModificationExtension;
import codeOrchestra.util.XMLUtils;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.ClassDefinitionNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        File digestsDir = new File("/Users/buildserver/TMP/digest/"); // TODO: make configurable!
        File[] digestFiles = digestsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(DIGEST_EXTENSION);
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

        // TODO: take fq names from SWCs
    }

    public void addToDigestUnresolved(CompilationUnit cu, ClassDefinitionNode classDefinitionNode) {
        if (classDefinitionNode == null) {
            return;
        }

        SourceClassDigest classDigest = new SourceClassDigest(classDefinitionNode);
        unresolvedDigests.put(classDigest.getFqName(), classDigest);
    }

    private File getSourceDigestsFolder() {
        File digestsFolder = new File(System.getProperty("java.io.tmpdir"), "coltDigests");

        if (!digestsFolder.exists()) {
            digestsFolder.mkdirs();
        }

        return digestsFolder;
    }

    public void resolve() {
        for (SourceClassDigest classDigest : unresolvedDigests.values()) {
            classDigest.resolve();
            digestsMap.put(classDigest.getFqName(), classDigest);
        }
        unresolvedDigests.clear();
    }
}
