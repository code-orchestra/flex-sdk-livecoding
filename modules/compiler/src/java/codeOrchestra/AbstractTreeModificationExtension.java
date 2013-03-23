package codeOrchestra;

import codeOrchestra.tree.ProjectNavigator;
import flex2.compiler.CompilationUnit;
import flex2.compiler.as3.Extension;
import flex2.compiler.as3.reflect.TypeTable;
import flex2.tools.Fcsh;
import macromedia.asc.parser.*;

import java.io.*;

/**
 * @author Anton.I.Neverov
 */
public abstract class AbstractTreeModificationExtension implements Extension {

    private static boolean TRACE = true;
    private static final String TEMP_DIR = "C:\\Temp\\serializedAST";
    public static final String SERIALIZED_AST = ".serializedAST";

    protected ProjectNavigator projectNavigator;

    private void saveSyntaxTree(CompilationUnit unit) {
        String shortName = unit.getSource().getShortName();
        String serializedPath = TEMP_DIR + File.separator + shortName + SERIALIZED_AST;
        Object syntaxTree = unit.getSyntaxTree();
        if (!(syntaxTree instanceof ProgramNode)) {
            throw new RuntimeException("Syntax tree of unit " + unit.getSource().getName() + " is not a ProgramNode, it is " + syntaxTree.getClass());
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serializedPath));
            oos.writeObject(syntaxTree);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSyntaxTrees() {
        if (projectNavigator != null) {
            return;
        }

        projectNavigator = new ProjectNavigator();

        File dir = new File(TEMP_DIR);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(SERIALIZED_AST);
            }
        });

        try {
            for (File file : files) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                Object restoredSyntaxTree = ois.readObject();
                ois.close();
                projectNavigator.loadedSyntaxTrees.add((ProgramNode) restoredSyntaxTree); // Safe cast. Type is checked on saving
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected abstract void performModifications(CompilationUnit unit);

    private void traceStep(String step, String unitPath) {
        if (TRACE && !unitPath.startsWith("C:")) {
            System.out.println(step + ": " + unitPath);
        }
    }

    @Override
    public final void parse1(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingTransformMode) {
            return;
        }
        if (Fcsh.livecodingTransformModeSecondPass) {
            loadSyntaxTrees();
            performModifications(unit);
        } else {
            saveSyntaxTree(unit);
        }
        traceStep("parse1", unit.getSource().getRawLocation());
    }

    @Override
    public final void parse2(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingTransformMode) {
            return;
        }
        traceStep("parse2", unit.getSource().getRawLocation());
    }

    @Override
    public final void analyze1(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingTransformMode) {
            return;
        }
        traceStep("analyze1", unit.getSource().getRawLocation());
    }

    @Override
    public final void analyze2(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingTransformMode) {
            return;
        }
        traceStep("analyze2", unit.getSource().getRawLocation());
    }

    @Override
    public final void analyze3(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingTransformMode) {
            return;
        }
        traceStep("analyze3", unit.getSource().getRawLocation());
    }

    @Override
    public final void analyze4(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingTransformMode) {
            return;
        }
        traceStep("analyze4", unit.getSource().getRawLocation());
    }

    @Override
    public final void generate(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingTransformMode) {
            return;
        }
        traceStep("generate", unit.getSource().getRawLocation());
    }
}
