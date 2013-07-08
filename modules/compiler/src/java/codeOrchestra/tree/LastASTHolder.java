package codeOrchestra.tree;

import macromedia.asc.parser.ProgramNode;
import org.apache.commons.lang3.SerializationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public class LastASTHolder {

    private static final boolean TRACE = false;

    private static final LastASTHolder instance = new LastASTHolder();

    public static LastASTHolder getInstance() {
        return instance;
    }

    private Map<String, ProgramNode> programNodes = new HashMap<String, ProgramNode>();

    private LastASTHolder() {
    }

    public void add(String fqName, ProgramNode programNode) {
        long timeStarted = System.currentTimeMillis();

//        ProgramNode clone = null;
//        try{
//            clone = programNode.clone();
//        }catch (CloneNotSupportedException ex)
//        {
//            System.out.println("[LastASTHolder#add] : " + ex.toString());
//        }
        ProgramNode clone = SerializationUtils.clone(programNode);

        if (TRACE) {
            System.out.println("Cloning of " + fqName + " AST took " + (System.currentTimeMillis() - timeStarted) + "ms");
        }

            /*
            clone.cx = null;
            for (RegularNode regularNode : new RegularNode(clone).getDescendants()) {
                Node astNode = regularNode.getASTNode();

                try {
                    Field cx = astNode.getClass().getField("cx");
                    cx.set(astNode, null);
                } catch (Throwable t) {
                    // ignore
                }
            }
            */

        programNodes.put(fqName, clone);

    }

    public Map<String, ProgramNode> getProgramNodes() {
        return programNodes;
    }

    public void clearCache() {
        programNodes.clear();
    }

    public Set<String> getAvailableFqNames() {
        return programNodes.keySet();
    }
}
