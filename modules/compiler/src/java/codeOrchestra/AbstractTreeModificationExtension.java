package codeOrchestra;

import codeOrchestra.digest.DigestManager;
import codeOrchestra.digest.IClassDigest;
import codeOrchestra.digest.IMember;
import codeOrchestra.digest.Visibility;
import codeOrchestra.tree.*;
import codeOrchestra.util.Pair;
import codeOrchestra.util.StringUtils;
import flex2.compiler.CompilationUnit;
import flex2.compiler.as3.Extension;
import flex2.compiler.as3.reflect.TypeTable;
import flex2.tools.Fcsh;
import macromedia.asc.parser.*;
import macromedia.asc.util.Context;
import macromedia.asc.util.ObjectList;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public abstract class AbstractTreeModificationExtension implements Extension {

    protected static boolean TRACE = false;
    public static final String SERIALIZED_AST = ".serializedAST";

    protected ProjectNavigator projectNavigator;

    /*
    public static String getCachesDir() {
        File serializedASTDir = new File(System.getProperty("java.io.tmpdir"), "serializedAST");

        if (!serializedASTDir.exists()) {
            serializedASTDir.mkdirs();
        }

        return serializedASTDir.getPath();
    }
    */

    protected void saveSyntaxTree(CompilationUnit unit) {
        ClassDefinitionNode classDefinition = TreeNavigator.getClassDefinition(unit);
        if (classDefinition == null) {
            return;
        }

        String fqName = StringUtils.longNameFromNamespaceAndShortName(classDefinition.pkgdef.name.id.pkg_part, classDefinition.name.name);

        Object syntaxTree = unit.getSyntaxTree();
        if (!(syntaxTree instanceof ProgramNode)) {
            throw new RuntimeException("Syntax tree of unit " + unit.getSource().getName() + " is not a ProgramNode, it is " + syntaxTree.getClass());
        }

        LastASTHolder.getInstance().add(fqName, (ProgramNode) syntaxTree);
        /*
        String serializedPath = getCachesDir() + File.separator + fqName + SERIALIZED_AST;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serializedPath));
            oos.writeObject(syntaxTree);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    protected void loadSyntaxTrees() {
        if (projectNavigator != null) {
            return;
        }

        projectNavigator = new ProjectNavigator();

        for (Map.Entry<String, ProgramNode> programNodeInfo : LastASTHolder.getInstance().getProgramNodes().entrySet()) {
            projectNavigator.add(programNodeInfo.getKey(), programNodeInfo.getValue());
        }

        /*
        File dir = new File(getCachesDir());
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
        */
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

        try {
            performModifications(unit);
        } catch (Throwable t) {
            System.out.println("Error during custom AST modifications:");
            t.printStackTrace();
        }

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

    protected String addLiveCodingClass(String className, ClassDefinitionNode parentClass, FunctionDefinitionNode functionDefinitionNode, ObjectList<Node> methodBody, boolean incremental) {
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

        String originalPackageName = functionDefinitionNode.pkgdef.name.id.pkg_part;
        String originalClassFqName = StringUtils.longNameFromNamespaceAndShortName(originalPackageName, className);

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
        classCONode.addImport("codeOrchestra.actionScript.logging.logUtil", "LogUtil");
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
        runMethod.isStatic = staticMethod;
        runMethod.statements.add(new ExpressionStatementNode(new ListNode(null,
                TreeUtil.createCall(
                        "LiveCodingCodeFlowUtil",
                        "checkRecursion",
                        new ArgumentListNode(new LiteralStringNode(packageName + "." + liveCodingClassName + ".run"), -1)
                ),
                -1)));
        StatementListNode tryblock = new StatementListNode(null);

        ParameterListNode parameters = functionDefinitionNode.fexpr.signature.parameter;
        if (parameters != null) {
            for (ParameterNode parameterNode : parameters.items) {
                if (parameterNode.type == null) {
                    runMethod.addParameter(parameterNode.identifier.name, null);
                } else {
                    runMethod.addParameter(parameterNode.identifier.name, ((IdentifierNode) ((MemberExpressionNode) ((TypeExpressionNode) parameterNode.type).expr).selector.expr).name);
                }
            }
        }

        methodBody.removeLast(); // Removes last ReturnStatement

        // COLT-104 - memorize the function scope variable definitions
        Set<String> localVariables = new HashSet<String>();
        for (Node statement : methodBody) {
            List<RegularNode> varBindingRegularNodes = new RegularNode(statement).getDescendants(VariableBindingNode.class, FunctionCommonNode.class);
            for (RegularNode varBindingRegularNode : varBindingRegularNodes) {
                VariableBindingNode varBindingNode = (VariableBindingNode) varBindingRegularNode.getASTNode();
                localVariables.add(varBindingNode.variable.identifier.name);
            }
        }
        if (functionDefinitionNode.fexpr.signature != null) {
            if (functionDefinitionNode.fexpr.signature.parameter != null) {
                for (ParameterNode item : functionDefinitionNode.fexpr.signature.parameter.items) {
                    if (item.identifier != null) {
                        localVariables.add(item.identifier.name);
                    }
                }
            }
        }

        Iterator<Node> iterator = methodBody.iterator();
        boolean addedGetTimerImport = false;
        // 'This' scope modifications
        List<Pair<Node>> deferredInsertions = new ArrayList<Pair<Node>>();
        for (Node statement : methodBody) {
            RegularNode statementRegularNode = new RegularNode(statement);

            // Replace all `this` references with `thisScope`
            if (!staticMethod) {
                List<RegularNode> thisNodes = statementRegularNode.getDescendants(ThisExpressionNode.class, FunctionCommonNode.class);
                for (RegularNode thisNode : thisNodes) {
                    thisNode.replace(TreeUtil.createIdentifier("thisScope"));
                }
            }

            // COLT-62 - Add loop checks
            List<RegularNode> loopStatements = statementRegularNode.getDescendants(new HashSet<Class>() {{
                add(ForStatementNode.class);
                add(WhileStatementNode.class);
                add(DoStatementNode.class);
            }});
            for (RegularNode loopStatementRegularNode : loopStatements) {
                Node loopASTNode = loopStatementRegularNode.getASTNode();
                if (loopASTNode instanceof HasBody && ((HasBody) loopASTNode).getBody() instanceof StatementListNode) {
                    StatementListNode loopBody = (StatementListNode) ((HasBody) loopASTNode).getBody();

                    // Var definition
                    String id = generateId();
                    Node initializer = new BinaryExpressionNode(Tokens.PLUS_TOKEN, new LiteralStringNode(id), TreeUtil.createCall(null, "getTimer", null));
                    String varName = "reqId" + id;
                    loopBody.items.add(0, TreeUtil.createLocalVariable(parentClass.pkgdef, varName, "String", initializer));

                    // LiveCodingCodeFlowUtil.checkLoop call
                    loopBody.items.add(1, new ExpressionStatementNode(new ListNode(null,
                            TreeUtil.createCall(
                                    "LiveCodingCodeFlowUtil",
                                    "checkLoop",
                                    new ArgumentListNode(TreeUtil.createIdentifier(varName), -1)
                            ),
                            -1)));

                    if (!addedGetTimerImport) {
                        classCONode.addImport("flash.utils", "getTimer");
                        addedGetTimerImport = true;
                    }
                }

                ExpressionStatementNode emptyCheckLoopStatement = new ExpressionStatementNode(new ListNode(null,
                        TreeUtil.createCall(
                                "LiveCodingCodeFlowUtil",
                                "checkLoop",
                                new ArgumentListNode(new LiteralStringNode(""), -1)
                        ),
                        -1));

                RegularNode parent = loopStatementRegularNode.getParent();
                if (parent == null) {
                    deferredInsertions.add(new Pair<Node>(loopASTNode, emptyCheckLoopStatement));
                } else if (parent.getASTNode() instanceof StatementListNode) {
                    StatementListNode parentBody = (StatementListNode) parent.getASTNode();
                    parentBody.items.add(parentBody.items.indexOf(loopASTNode) + 1, emptyCheckLoopStatement);
                }
            }

            // COLT-25 - Replace all field/method references without 'this.' to 'thisScope.x'
            List<RegularNode> memberExpressionNodes = statementRegularNode.getDescendants(MemberExpressionNode.class);
            for (RegularNode regularMemberExprNode : memberExpressionNodes) {
                MemberExpressionNode memberExpression = (MemberExpressionNode) regularMemberExprNode.getASTNode();
                if (memberExpression.base == null) {
                    SelectorNode selector = memberExpression.selector;

                    // COLT-104 - skip transforming the local variable references
                    if (selector != null && selector.getIdentifier() != null && localVariables.contains(selector.getIdentifier().name)) {
                        continue;
                    }

                    // COLT-38 - Transform trace() to LogUtil.log()
                    if (selector instanceof CallExpressionNode) {
                        if ("trace".equals(selector.getIdentifier().name)) {
                            CallExpressionNode callExpressionNode = (CallExpressionNode) selector;

                            ArgumentListNode errorTraceArguments = new ArgumentListNode(new LiteralStringNode("trace"), -1);
                            errorTraceArguments.items.add(new LiteralStringNode(""));
                            errorTraceArguments.items.add(new LiteralStringNode(""));
                            errorTraceArguments.items.add(new LiteralStringNode(originalClassFqName));
                            errorTraceArguments.items.add(new BinaryExpressionNode(Tokens.PLUS_TOKEN, new LiteralStringNode(""), callExpressionNode.args.items.get(0)));

                            callExpressionNode.expr = new IdentifierNode("log", -1);
                            callExpressionNode.args = errorTraceArguments;

                            memberExpression.base = TreeUtil.createIdentifier("LogUtil");
                            continue;
                        }
                    }

                    IdentifierNode identifier = selector.getIdentifier();
                    if (identifier != null) {
                        if (!staticMethod && DigestManager.getInstance().isInstanceMemberVisibleInsideClass(originalClassFqName, identifier.name)) {
                            memberExpression.base = TreeUtil.createIdentifier("thisScope");
                        } else if (DigestManager.getInstance().findOwnerOfStaticMember(originalClassFqName, identifier.name) != null) {
                            String ownerOfStaticMemberFqName = DigestManager.getInstance().findOwnerOfStaticMember(originalClassFqName, identifier.name);
                            String shortName = StringUtils.shortNameFromLongName(ownerOfStaticMemberFqName);
                            memberExpression.base = TreeUtil.createIdentifier(shortName);
                            // TODO: add import!
                        } else {
                            String possibleClassName = identifier.name;
                            if (className.equals(possibleClassName)) {
                                continue;
                            }

                            Set<String> shortNamesFromPackage = DigestManager.getInstance().getShortNamesFromPackage(originalPackageName);
                            if (shortNamesFromPackage != null && shortNamesFromPackage.contains(possibleClassName)) {
                                selector.expr = new QualifiedIdentifierNode(new LiteralStringNode(originalPackageName), possibleClassName, -1);
                            }
                        }
                    }
                }
            }

            // COLT-34 redirect protected/super references
            for (RegularNode regularMemberExprNode : memberExpressionNodes) {
                MemberExpressionNode memberExpression = (MemberExpressionNode) regularMemberExprNode.getASTNode();
                Node base = memberExpression.base;
                if (base == null) {
                    continue;
                }

                if (base instanceof MemberExpressionNode) {
                    // thisScope.protectedMember
                    MemberExpressionNode memberExpressionBase = (MemberExpressionNode) base;

                    if (memberExpressionBase.base == null && memberExpressionBase.selector.getIdentifier().name.equals("thisScope")) {
                        String accessorName = memberExpression.selector.getIdentifier().name;
                        IClassDigest visibleOwnerInsideClass = DigestManager.getInstance().findVisibleOwnerOfInstanceMember(originalClassFqName, accessorName);
                        if (visibleOwnerInsideClass != null) {
                            IMember instanceMember = visibleOwnerInsideClass.getInstanceMember(accessorName);
                            if (instanceMember.getVisibility() == Visibility.PROTECTED) {
                                String newAccessorName = accessorName + "_protected" + DigestManager.getInstance().getInheritanceLevel(visibleOwnerInsideClass.getFqName());
                                memberExpression.selector.getIdentifier().name = newAccessorName;
                            }
                        }
                    }
                } else if (base instanceof SuperExpressionNode) {
                    // super.someMember
                    String accessorName = memberExpression.selector.getIdentifier().name;
                    IClassDigest visibleOwnerInsideClass = DigestManager.getInstance().findVisibleOwnerOfInstanceMember(originalClassFqName, accessorName);
                    if (visibleOwnerInsideClass != null) {
                        String newAccessorName = accessorName + "_overriden_super" + DigestManager.getInstance().getInheritanceLevel(visibleOwnerInsideClass.getFqName());
                        memberExpression.selector.getIdentifier().name = newAccessorName;
                        memberExpression.base = TreeUtil.createIdentifier("thisScope");
                    }
                }
            }
        }
        for (Pair<Node> deferredInsertion : deferredInsertions) {
            methodBody.add(methodBody.indexOf(deferredInsertion.getO1()) + 1, deferredInsertion.getO2());
        }

        // Wrap it in try/catch and log the error in catch
        tryblock.items.addAll(methodBody);
        ArgumentListNode errorTraceArguments = new ArgumentListNode(new LiteralStringNode("error"), -1);
        errorTraceArguments.items.add(new LiteralStringNode(""));
        errorTraceArguments.items.add(new LiteralStringNode(""));
        errorTraceArguments.items.add(new LiteralStringNode(classCONode.getFQName()));
        errorTraceArguments.items.add(
                new BinaryExpressionNode(Tokens.PLUS_TOKEN, new LiteralStringNode("live method " + LiveCodingUtil.constructLiveCodingMethodId(functionDefinitionNode, className) + " execute error: "), TreeUtil.createIdentifier("e")));
        errorTraceArguments.items.add(TreeUtil.createIdentifier("e"));
        StatementListNode catchlist = new StatementListNode(new CatchClauseNode(
                TreeUtil.createParameterNode("e", "Error"),
                new StatementListNode(new ExpressionStatementNode(new ListNode(null,
                        TreeUtil.createCall(
                                "LogUtil",
                                "log",
                                errorTraceArguments
                        ),
                        -1))))
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

        // COLT-109
        ClassDefinitionNode liveClassNode = classCONode.addToProject();
        for (Node pkgStatement : functionDefinitionNode.pkgdef.statements.items) {
            if (pkgStatement instanceof UseDirectiveNode) {
                liveClassNode.pkgdef.statements.items.add(pkgStatement);
            }
        }
        for (Node classStatement : parentClass.statements.items) {
            if (classStatement instanceof UseDirectiveNode) {
                liveClassNode.statements.items.add(classStatement);
            }
        }

        return liveCodingClassName;
    }

    protected String generateId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
