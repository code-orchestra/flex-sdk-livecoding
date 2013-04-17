package codeOrchestra.digest;

import codeOrchestra.AbstractTreeModificationExtension;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.ClassDefinitionNode;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

/**
 * @author Alexander Eliseyev
 */
public class DigestManager {

    private static DigestManager instance = new DigestManager();
    public static DigestManager getInstance() {
        return instance;
    }

    private Set<String> availableFqNames = new HashSet<String>();
    private Map<String, Document> digestsMap = new HashMap<String, Document>();

    private Map<String, ClassDigest> unresolvedDigests = new HashMap<String, ClassDigest>();

    public boolean isAvailable(String fqName) {
        return availableFqNames.contains(fqName);
    }

    public void initAvailableFqNames() {
        availableFqNames.clear();

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

        // TODO: take fq names from SWCs
    }

    public void addToDigestUnresolved(CompilationUnit cu, ClassDefinitionNode classDefinitionNode) {
        if (classDefinitionNode == null) {
            return;
        }

        ClassDigest classDigest = new ClassDigest(classDefinitionNode);
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
        for (ClassDigest classDigest : unresolvedDigests.values()) {
            classDigest.resolve();
        }
    }
}
