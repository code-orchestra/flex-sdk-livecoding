package codeOrchestra;

import codeOrchestra.tree.*;
import flex2.compiler.CompilationUnit;
import flex2.compiler.as3.Extension;
import flex2.compiler.as3.reflect.TypeTable;
import flex2.tools.Fcsh;
import macromedia.asc.parser.*;
import macromedia.asc.util.Context;
import macromedia.asc.util.ObjectList;

import java.io.*;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public abstract class AbstractTreeModificationExtension implements Extension {

    protected static boolean TRACE = true;
    public static final String SERIALIZED_AST = ".serializedAST";

    protected ProjectNavigator projectNavigator;

    private static String getTmpDir() {
        File serializedASTDir = new File(System.getProperty("java.io.tmpdir"), "serializedAST");

        if (!serializedASTDir.exists()) {
            serializedASTDir.mkdirs();
        }

        return serializedASTDir.getPath();
    }

    protected void saveSyntaxTree(CompilationUnit unit) {
        String shortName = unit.getSource().getShortName();
        String serializedPath = getTmpDir() + File.separator + shortName + SERIALIZED_AST;
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

    protected void loadSyntaxTrees() {
        if (projectNavigator != null) {
            return;
        }

        projectNavigator = new ProjectNavigator();

        File dir = new File(getTmpDir());
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
        if (!(Fcsh.livecodingBaseMode || Fcsh.livecodingIncrementalMode)) { // Extra check
            return;
        }
        performModifications(unit);
        traceStep("parse1", unit.getSource().getRawLocation());
    }

    @Override
    public final void parse2(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingBaseMode) {
            return;
        }
        traceStep("parse2", unit.getSource().getRawLocation());
    }

    @Override
    public final void analyze1(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingBaseMode) {
            return;
        }
        traceStep("analyze1", unit.getSource().getRawLocation());
    }

    @Override
    public final void analyze2(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingBaseMode) {
            return;
        }
        traceStep("analyze2", unit.getSource().getRawLocation());
    }

    @Override
    public final void analyze3(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingBaseMode) {
            return;
        }
        traceStep("analyze3", unit.getSource().getRawLocation());
    }

    @Override
    public final void analyze4(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingBaseMode) {
            return;
        }
        traceStep("analyze4", unit.getSource().getRawLocation());
    }

    @Override
    public final void generate(CompilationUnit unit, TypeTable typeTable) {
        if (!Fcsh.livecodingBaseMode) {
            return;
        }
        traceStep("generate", unit.getSource().getRawLocation());
    }

    protected String addLiveCodingClass(String className, FunctionDefinitionNode functionDefinitionNode, ObjectList<Node> methodBody, boolean incremental) {
        boolean staticMethod = TreeNavigator.isStaticMethod(functionDefinitionNode);
        TypeExpressionNode methodResult = (TypeExpressionNode) functionDefinitionNode.fexpr.signature.result;
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String liveCodingClassName;
        if (incremental) {
            liveCodingClassName = "Method_" + className + "_" + functionDefinitionNode.name.identifier.name + "_" + timeStamp;
        } else {
            liveCodingClassName = LiveCodingUtil.constructLiveCodingClassName(functionDefinitionNode, className);
        }
        Context cx = functionDefinitionNode.cx;
        String packageName;
        if (incremental) {
            packageName = "codeOrchestra.liveCoding.load";
        } else {
            packageName = functionDefinitionNode.pkgdef.name.id.pkg_part;
        }
        ClassCONode classCONode = new ClassCONode(packageName, liveCodingClassName, cx);

        List<ImportDirectiveNode> imports = TreeNavigator.getImports(functionDefinitionNode.pkgdef);
        for (ImportDirectiveNode anImport : imports) {
            classCONode.addImport(anImport.name.id.pkg_part, anImport.name.id.def_part);
        }
        classCONode.addImport("codeOrchestra.actionScript.liveCoding.util", "LiveCodingCodeFlowUtil");
        classCONode.addImport("codeOrchestra.actionScript.liveCoding.util", "LiveCodeRegistry");
        if (incremental) {
            classCONode.addImport(functionDefinitionNode.pkgdef.name.id.pkg_part, className);
            classCONode.addImport("codeOrchestra.actionScript.liveCoding.util", "IMethodCodeUpdate");
            classCONode.interfaces.add("IMethodCodeUpdate");
        }
        MemberExpressionNode memberExpressionNode = null;

        if (!incremental) {
            /*
                   {
                     LiveCodeRegistry.getInstance().putMethod("com.example.Main.foo", LiveMethod_com_example_Main_foo);
                   }
                */
            ArgumentListNode args = new ArgumentListNode(
                    new LiteralStringNode(LiveCodingUtil.constructLiveCodingMethodId(functionDefinitionNode, className)),
                    -1
            );
            args.items.add(TreeUtil.createIdentifier(liveCodingClassName));
            memberExpressionNode = new MemberExpressionNode(
                    TreeUtil.createCall("LiveCodeRegistry", "getInstance", null),
                    new CallExpressionNode(new IdentifierNode("putMethod", -1), args),
                    -1
            );
            ExpressionStatementNode expressionStatementNode = new ExpressionStatementNode(new ListNode(null, memberExpressionNode, -1));
            classCONode.staticInitializer.add(expressionStatementNode);
        }

        if (!staticMethod) {
            /*
               public var thisScope : Main;
            */
            classCONode.fields.add(new FieldCONode("thisScope", className));

            /*
               public function LiveMethod_com_example_Main_foo( thisScope : Main ){
                   this.thisScope = thisScope;
               }
            */
            MethodCONode constructor = new MethodCONode(liveCodingClassName, null, cx);
            constructor.addParameter("thisScope", className);
            memberExpressionNode = new MemberExpressionNode(
                    new ThisExpressionNode(),
                    new SetExpressionNode(
                            new IdentifierNode("thisScope", -1),
                            new ArgumentListNode(TreeUtil.createIdentifier("thisScope"), -1)
                    ),
                    -1
            );
            constructor.statements.add(new ListNode(null, memberExpressionNode, -1));
            constructor.statements.add(new ReturnStatementNode(null));
            classCONode.methods.add(constructor);
        }

        /*
            public function run (  ) : void {
                LiveCodingCodeFlowUtil.checkRecursion("com.example.LiveMethod_com_example_Main_foo.run");
                try {
                    ...
                } catch ( e : Error ) {

                }
                [return null;]
            }
         */

        MethodCONode runMethod = new MethodCONode(
                "run",
                methodResult == null ? null : ((IdentifierNode) ((MemberExpressionNode) methodResult.expr).selector.expr).name, // TODO: rewrite
                cx
        );
        runMethod.statements.add(new ExpressionStatementNode(new ListNode(null,
                TreeUtil.createCall(
                        "LiveCodingCodeFlowUtil",
                        "checkRecursion",
                        new ArgumentListNode(new LiteralStringNode(packageName + "." + liveCodingClassName + ".run"), -1)
                ),
                -1)));
        StatementListNode tryblock = new StatementListNode(null);
        methodBody.removeLast(); // Removes last ReturnStatement

        if (!staticMethod) {
            // Replace all `this` references with `thisScope`
            for (Node node : methodBody) {
                RegularNode regularNode = new RegularNode(node);
                List<RegularNode> thisNodes = regularNode.getDescendants(ThisExpressionNode.class);
                for (RegularNode thisNode : thisNodes) {
                    thisNode.replace(TreeUtil.createIdentifier("thisScope"));
                }
            }
        }

        tryblock.items.addAll(methodBody);
        StatementListNode catchlist = new StatementListNode(new CatchClauseNode(
                TreeUtil.createParameterNode("e", "Error"),
                new StatementListNode(null))
        );
        runMethod.statements.add(new TryStatementNode(tryblock, catchlist, null));
        if (methodResult != null) {
            runMethod.statements.add(new ReturnStatementNode(new ListNode(null, new LiteralNullNode(), -1)));
        }
        runMethod.statements.add(new ReturnStatementNode(null));
        classCONode.methods.add(runMethod);

        if (incremental) {
            /*
                public function getMethodId (  ) : String {
                    return "com.example.Main.foo";
                }
             */
            MethodCONode getMethodIdNode = new MethodCONode("getMethodId", "String", cx);
            getMethodIdNode.statements.add(new ReturnStatementNode(new ListNode(
                    null,
                    new LiteralStringNode(LiveCodingUtil.constructLiveCodingMethodId(functionDefinitionNode, className)),
                    -1
            )));
            getMethodIdNode.statements.add(new ReturnStatementNode(null));
            classCONode.methods.add(getMethodIdNode);

            /*
               public function getMethodUpdateTime (  ) : String {
                   return "1361962995859";
               }
            */
            MethodCONode getMethodUpdateTime = new MethodCONode("getMethodUpdateTime", "String", cx);
            getMethodUpdateTime.statements.add(new ReturnStatementNode(new ListNode(
                    null,
                    new LiteralStringNode(timeStamp),
                    -1
            )));
            getMethodUpdateTime.statements.add(new ReturnStatementNode(null));
            classCONode.methods.add(getMethodUpdateTime);
        }

        classCONode.addToProject();

        return liveCodingClassName;
    }
}
