package codeOrchestra;

import codeOrchestra.digest.DigestManager;
import codeOrchestra.tree.*;
import codeOrchestra.util.Pair;
import codeOrchestra.util.StringUtils;
import flex2.compiler.CompilationUnit;
import flex2.compiler.as3.Extension;
import flex2.compiler.as3.reflect.TypeTable;
import flex2.tools.Fcsh;
import macromedia.asc.parser.*;
import macromedia.asc.semantics.Value;
import macromedia.asc.util.Context;
import macromedia.asc.util.ObjectList;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

/**
 * @author Anton.I.Neverov
 * @author Alexander Eliseyev
 */
public abstract class AbstractTreeModificationExtension implements Extension {

    protected static boolean TRACE = false;

    protected ProjectNavigator projectNavigator;

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
    }

    protected void loadSyntaxTrees() {
        if (projectNavigator != null) {
            return;
        }

        projectNavigator = new ProjectNavigator();

        for (Map.Entry<String, ProgramNode> programNodeInfo : LastASTHolder.getInstance().getProgramNodes().entrySet()) {
            projectNavigator.add(programNodeInfo.getKey(), programNodeInfo.getValue());
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
        runMethod.setNamespaceVisibility(TreeNavigator.getNamespaceVisibility(functionDefinitionNode));
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
            ParameterListNode parametersClone = SerializationUtils.clone(parameters);


            for (ParameterNode parameterNode : parameters.items) {
//              Node initializer = parameterNode.init == null ? null : SerializationUtils.clone(parameterNode.init);
                Node initializer = null;
                if (parameterNode.type == null) {
                    runMethod.addParameter(parameterNode.identifier.name, null, initializer);
                } else {
                    runMethod.addParameter(parameterNode.identifier.name, (TypeExpressionNode) parameterNode.type, initializer);
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

        // 'This' scope modifications
        List<Pair<Node, Node>> deferredInsertions = new ArrayList<Pair<Node, Node>>();
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
                LoopStatement loopASTNode = (LoopStatement) loopStatementRegularNode.getASTNode();
                Transformations.transformLoopStatement(parentClass, classCONode, deferredInsertions, loopStatementRegularNode, loopASTNode);
            }

            // COLT-25 - Replace all field/method references without 'this.' to 'thisScope.x'
            List<RegularNode> memberExpressionNodes = statementRegularNode.getDescendants(MemberExpressionNode.class);
            for (RegularNode regularMemberExprNode : memberExpressionNodes) {
                MemberExpressionNode memberExpression = (MemberExpressionNode) regularMemberExprNode.getASTNode();
                Transformations.transformMemberReferences(className, staticMethod, originalPackageName, originalClassFqName, localVariables, memberExpression);
            }

            // COLT-34 redirect protected/super references
            for (RegularNode regularMemberExprNode : memberExpressionNodes) {
                MemberExpressionNode memberExpression = (MemberExpressionNode) regularMemberExprNode.getASTNode();
                Transformations.transformProtectedAndSuperReferences(originalClassFqName, memberExpression);
            }
        }
        for (Pair<Node, Node> deferredInsertion : deferredInsertions) {
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
                TreeUtil.createParameterNode("e", "Error", null),
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


        // Copy namespace declarations to the live class
        Set<String> namespaces = new HashSet<String>();
        ClassDefinitionNode classDefinitionNode = classCONode.addToProject();
        for (Node pkgStatement : parentClass.pkgdef.statements.items) {
            if (pkgStatement instanceof UseDirectiveNode) {
                namespaces.add(TreeNavigator.getNamespaceName((UseDirectiveNode) pkgStatement));
            }
        }
        for (Node classStatement : parentClass.statements.items) {
            if (classStatement instanceof UseDirectiveNode) {
                namespaces.add(TreeNavigator.getNamespaceName((UseDirectiveNode) classStatement));
            }
        }
        for (String namespace : namespaces) {
            String namespaceURI = DigestManager.getInstance().getNamespaceURI(namespace);
            if (namespaceURI != null) {
                classDefinitionNode.statements.items.add(0, new NamespaceDefinitionNode(classDefinitionNode.pkgdef, null, new IdentifierNode(namespace, -1), new LiteralStringNode(namespaceURI)));
            }
        }
        for (Node classStatement : parentClass.statements.items) {
            if (classStatement instanceof NamespaceDefinitionNode) {
                NamespaceDefinitionNode namespaceDefinitionNode = (NamespaceDefinitionNode) SerializationUtils.clone(classStatement);
                namespaceDefinitionNode.pkgdef = classDefinitionNode.pkgdef;
                classDefinitionNode.statements.items.add(namespaceDefinitionNode);
                namespaces.add(namespaceDefinitionNode.name.name);
            }
        }
        // Insert use namespace directives in the run methods
        for (Node item : classDefinitionNode.statements.items) {
            if (item instanceof FunctionDefinitionNode) {
                if (((FunctionDefinitionNode) item).fexpr.identifier.name.equals("run")) {
                    for (String namespace : namespaces) {
                        (((FunctionDefinitionNode) item)).fexpr.body.items.add(0, new UseDirectiveNode(classDefinitionNode.pkgdef, null, TreeUtil.createIdentifier(namespace)));
                    }
                }
            }
        }

        return liveCodingClassName;
    }

    protected FunctionDefinitionNode addLiveInitializerMethod(ClassDefinitionNode classDefinitionNode, boolean isIncremental) {
        String className = classDefinitionNode.name.name;

        // Create the method
        String initializerName = "live_initialize_" + className;
        MethodCONode liveInitializer = new MethodCONode(initializerName, null, classDefinitionNode.cx);
        FunctionDefinitionNode liveInitializerNode = liveInitializer.getFunctionDefinitionNode();
        liveInitializerNode.pkgdef = classDefinitionNode.pkgdef;
        classDefinitionNode.statements.items.add(liveInitializerNode);

        if (!isIncremental) {
            // Add metadata
            MetaDataNode listenerMetadata = new MetaDataNode(new LiteralArrayNode(null));
            listenerMetadata.setId("LiveCodeUpdateListener");
            listenerMetadata.setValues(new Value[] { new MetaDataEvaluator.KeyValuePair("method", initializerName) });
            liveInitializerNode.metaData = new StatementListNode(listenerMetadata);

            // Make it live
            String livecodingClassName = extractMethodToLiveCodingClass(liveInitializerNode, classDefinitionNode);

            // Add its call to the constructor
            FunctionDefinitionNode constructorDefinition = TreeUtil.getOrCreateConstructor(classDefinitionNode);
            ObjectList<Node> constructorBody = constructorDefinition.fexpr.body.items;
            constructorBody.add(constructorBody.size() - 1, new ExpressionStatementNode(new ListNode(null, TreeUtil.createCall(null, initializerName, null), -1)));

            projectNavigator.addAdditionalLivecodingClass(classDefinitionNode.pkgdef.name.id.pkg_part, className, livecodingClassName);
        }

        return liveInitializerNode;
    }

    /**
     * @return live coding class name
     */
    protected String extractMethodToLiveCodingClass(FunctionDefinitionNode functionDefinitionNode, ClassDefinitionNode classDefinitionNode) {
        ObjectList<Node> oldBody = functionDefinitionNode.fexpr.body.items;
        functionDefinitionNode.fexpr.body.items = new ObjectList<Node>();

        String className = classDefinitionNode.name.name;
        fillStubMethodBody(functionDefinitionNode, className);

        String livecodingClassName = addLiveCodingClass(className, classDefinitionNode, functionDefinitionNode, oldBody, false);

        if (LiveCodingUtil.isLiveCodeUpdateListener(functionDefinitionNode)) {
            addListener(functionDefinitionNode, classDefinitionNode);
        }

        return livecodingClassName;
    }

    private void fillStubMethodBody(FunctionDefinitionNode functionDefinitionNode, String className) {
        boolean staticMethod = TreeNavigator.isStaticMethod(functionDefinitionNode);
        boolean isVoid = functionDefinitionNode.fexpr.signature.result == null;
        ObjectList<Node> newBody = functionDefinitionNode.fexpr.body.items;
        PackageDefinitionNode pkgdef = functionDefinitionNode.pkgdef;

        /*
            var method : Class  = LiveCodeRegistry.getInstance().getMethod("com.example.Main.foo");
            or
            var method : *  = LiveCodeRegistry.getInstance().getMethod("com.example.Main.static.getColor");
         */

        TypeExpressionNode methodType = staticMethod ? null : new TypeExpressionNode(
                TreeUtil.createIdentifier("Class"),
                true,
                false
        );
        TypedIdentifierNode variable = new TypedIdentifierNode(
                new QualifiedIdentifierNode(null, "method", -1),
                methodType,
                -1
        );
        String remoteMethodName = LiveCodingUtil.constructLiveCodingMethodId(functionDefinitionNode, className);
        MemberExpressionNode initializer = new MemberExpressionNode(
                TreeUtil.createCall("LiveCodeRegistry", "getInstance", null),
                new CallExpressionNode(
                        new IdentifierNode("getMethod", -1),
                        new ArgumentListNode(new LiteralStringNode(remoteMethodName), -1)
                ),
                -1
        );
        ListNode listNode = new ListNode(
                null,
                new VariableBindingNode(pkgdef, null, Tokens.VAR_TOKEN, variable, initializer),
                -1
        );
        newBody.add(new VariableDefinitionNode(pkgdef, null, Tokens.VAR_TOKEN, listNode, -1));

        /*
            new LiveMethod_com_example_Main_foo();
         */
//        ArgumentListNode args = null;
//        if (!staticMethod) {
//            args = new ArgumentListNode(new ThisExpressionNode(),-1);
//        }
//        CallExpressionNode testCall = new CallExpressionNode(new IdentifierNode(LiveCodingUtil.constructLiveCodingClassName(functionDefinitionNode, className), -1), args);
//        testCall.is_new = true;
//        newBody.add(new ExpressionStatementNode(new ListNode(null, new MemberExpressionNode(null, testCall, -1), -1)));

        /*
            [return] (new method(this)).run();
            or
            [return] method.run();
         */

        // COLT-57
        ArgumentListNode argumentListNode = null;
        ParameterListNode parameterListNode = functionDefinitionNode.fexpr.signature.parameter;
        if (parameterListNode != null && parameterListNode.items.size() > 0) {
            argumentListNode = new ArgumentListNode(TreeUtil.createIdentifier(parameterListNode.items.get(0).identifier.name), -1);
            if (parameterListNode.items.size() > 1) {
                for (int i = 1; i < parameterListNode.items.size(); i++) {
                    argumentListNode.items.add(TreeUtil.createIdentifier(parameterListNode.items.get(i).identifier.name));
                }
            }
        }
        ListNode expr;
        if (!staticMethod) {
            CallExpressionNode callExpressionNode = new CallExpressionNode(
                    new IdentifierNode("method", -1),
                    new ArgumentListNode(new ThisExpressionNode(), -1)
            );
            callExpressionNode.is_new = true;
            ListNode base = new ListNode(null, new MemberExpressionNode(null, callExpressionNode, -1), -1);
            CallExpressionNode selector = new CallExpressionNode(new IdentifierNode("run", -1), argumentListNode);
            expr = new ListNode(null, new MemberExpressionNode(base, selector, -1), -1);
        } else {
            expr = new ListNode(null, TreeUtil.createCall("method", "run", argumentListNode), -1);
        }
        if (isVoid) {
            newBody.add(new ExpressionStatementNode(expr));
        } else {
            newBody.add(new ReturnStatementNode(expr));
        }

        /*
            return;
         */
        newBody.add(new ReturnStatementNode(null));
    }

    private void addListener(FunctionDefinitionNode functionDefinitionNode, ClassDefinitionNode classDefinitionNode) {
        /*
            public function foo_codeUpdateListener4703382380319456072 ( e : MethodUpdateEvent ) : void {
                if ( e.classFqn == "com.example.Main" ) {
                    foo();
                }
            }
         */

        String classFqn = StringUtils.longNameFromNamespaceAndShortName(functionDefinitionNode.pkgdef.name.id.pkg_part, classDefinitionNode.name.name);
        String methodNameToFilter = null;
        boolean weak = true;
        int priority = 0;

        MetaDataNode annotation = TreeNavigator.getAnnotation(functionDefinitionNode, "LiveCodeUpdateListener");
        if (annotation != null) {
            String annotationClassFqn = annotation.getValue("classFqn");
            if (!StringUtils.isEmpty(annotationClassFqn)) {
                classFqn = annotationClassFqn;
            }

            String annotationMethod = annotation.getValue("method");
            if (!StringUtils.isEmpty(annotationMethod)) {
                methodNameToFilter = annotationMethod;
            }

            String annotationWeak = annotation.getValue("weak");
            if (!StringUtils.isEmpty(annotationWeak)) {
                weak = Boolean.parseBoolean(annotationWeak);
            }

            String annotationPriority = annotation.getValue("priority");
            if (!StringUtils.isEmpty(annotationPriority)) {
                priority = Integer.parseInt(annotationPriority);
            }
        }

        boolean staticMethod = TreeNavigator.isStaticMethod(functionDefinitionNode);
        String id = StringUtils.generateId();
        String listenerName = functionDefinitionNode.name.identifier.name + "_codeUpdateListener" + id;
        MethodCONode listener = new MethodCONode(listenerName, null, classDefinitionNode.cx);
        listener.addParameter("e", "MethodUpdateEvent");

        Node firstStatement = null;
        MemberExpressionNode methodCall = TreeUtil.createCall(null, functionDefinitionNode.name.identifier.name, null);
        if ("*".equals(classFqn)) {
            firstStatement = new ExpressionStatementNode(new ListNode(null, methodCall, -1));
        } else {
            ListNode condition = new ListNode(
                    null,
                    new BinaryExpressionNode(
                            Tokens.EQUALS_TOKEN,
                            TreeUtil.createIdentifier("e", "classFqn"),
                            new LiteralStringNode(classFqn)
                    ),
                    -1
            );
            StatementListNode thenactions = new StatementListNode(new ExpressionStatementNode(new ListNode(
                    null,
                    methodCall,
                    -1
            )));
            firstStatement = new IfStatementNode(condition, thenactions, null);
        }

        if (!StringUtils.isEmpty(methodNameToFilter)) {
            ListNode condition;
            if (methodNameToFilter.contains(",")) {
                String[] split = methodNameToFilter.split(",");

                BinaryExpressionNode orExpression = new BinaryExpressionNode(
                        Tokens.EQUALS_TOKEN,
                        TreeUtil.createIdentifier("e", "methodName"),
                        new LiteralStringNode(split[0].trim())
                );

                for (int i = 1; i < split.length; i++) {
                    String methodName = split[i].trim();
                    orExpression = new BinaryExpressionNode(Tokens.LOGICALOR_TOKEN, orExpression, new BinaryExpressionNode(
                            Tokens.EQUALS_TOKEN,
                            TreeUtil.createIdentifier("e", "methodName"),
                            new LiteralStringNode(methodName)
                    ));
                }

                condition = new ListNode(
                        null,
                        orExpression,
                        -1
                );
            } else {
                condition = new ListNode(
                        null,
                        new BinaryExpressionNode(
                                Tokens.EQUALS_TOKEN,
                                TreeUtil.createIdentifier("e", "methodName"),
                                new LiteralStringNode(methodNameToFilter)
                        ),
                        -1
                );
            }
            StatementListNode thenactions = new StatementListNode(new ExpressionStatementNode(new ListNode(
                    null,
                    firstStatement,
                    -1
            )));
            firstStatement = new IfStatementNode(condition, thenactions, null);
        }

        listener.statements.add(firstStatement);
        listener.statements.add(new ReturnStatementNode(null));
        listener.isStatic = staticMethod;
        classDefinitionNode.statements.items.add(listener.getFunctionDefinitionNode());

        /*
            LiveCodeRegistry.getInstance().addEventListener(MethodUpdateEvent.METHOD_UPDATE, this.foo_codeUpdateListener4703382380319456072, false, 0, true);
            or
            LiveCodeRegistry.getInstance().addEventListener(MethodUpdateEvent.METHOD_UPDATE, Main.getColor_codeUpdateListener2123954341648648741, false, 0, true);
         */

        ArgumentListNode args = new ArgumentListNode(TreeUtil.createIdentifier("MethodUpdateEvent", "METHOD_UPDATE"), -1);
        MemberExpressionNode qListenerName = staticMethod ? TreeUtil.createIdentifier(classDefinitionNode.name.name, listenerName) : TreeUtil.createThisIdentifier(listenerName);
        args.items.add(qListenerName);
        args.items.add(new LiteralBooleanNode(false));
        args.items.add(new LiteralNumberNode(String.valueOf(priority)));
        args.items.add(new LiteralBooleanNode(weak));
        CallExpressionNode selector = new CallExpressionNode(new IdentifierNode("addEventListener", -1), args);
        MemberExpressionNode item = new MemberExpressionNode(TreeUtil.createCall("LiveCodeRegistry", "getInstance", null), selector, -1);
        ExpressionStatementNode listenerAddExpressionStatement = new ExpressionStatementNode(new ListNode(null, item, -1));

        FunctionDefinitionNode constructorDefinition = TreeUtil.getOrCreateConstructor(classDefinitionNode);

        ObjectList<Node> constructorBody = constructorDefinition.fexpr.body.items;
        constructorBody.add(constructorBody.size() - 1, listenerAddExpressionStatement);
    }

}
