/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package flex2.compiler.mxml;

import flex2.compiler.Source;
import flex2.compiler.as3.AbstractSyntaxTreeUtil;
import flex2.compiler.as3.As3Compiler;
import flex2.compiler.as3.BytecodeEmitter;
import flex2.compiler.mxml.lang.FrameworkDefs;
import flex2.compiler.mxml.rep.DocumentInfo;
import flex2.compiler.mxml.rep.VariableDeclaration;
import macromedia.asc.embedding.ConfigVar;
import macromedia.asc.parser.*;
import macromedia.asc.util.ContextStatics;
import macromedia.asc.util.ObjectList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This class handles the direct AST generation for the first pass
 * skeleton implemenation.
 *
 */
public class InterfaceGenerator extends AbstractGenerator
{
    private DocumentInfo docInfo;

    public InterfaceGenerator(DocumentInfo docInfo, Set<String> bogusImports,
                              ContextStatics contextStatics, Source source,
                              BytecodeEmitter bytecodeEmitter,
                              ObjectList<ConfigVar> defines)
    {
        super(docInfo.getStandardDefs());

        this.docInfo = docInfo;
        context = AbstractSyntaxTreeUtil.generateContext(contextStatics, source,
                                                         bytecodeEmitter, defines);
        nodeFactory = context.getNodeFactory();

        configNamespaces = new HashSet<>();
        StatementListNode configVars = AbstractSyntaxTreeUtil.parseConfigVars(context, configNamespaces);
        programNode = AbstractSyntaxTreeUtil.generateProgram(context, configVars, docInfo.getPackageName());
        StatementListNode programStatementList = programNode.statements;

        programStatementList = generateImports(programStatementList, bogusImports);
        programStatementList = generateMetaData(programStatementList, docInfo.getMetadata());
        
        ClassDefinitionNode classDefinition = generateClassDefinition();
        programStatementList = nodeFactory.statementList(programStatementList, classDefinition);

        programNode.statements = programStatementList;

        PackageDefinitionNode packageDefinition = nodeFactory.finishPackage(context, null);
        nodeFactory.statementList(programStatementList, packageDefinition);

        // Useful when comparing abstract syntax trees
        //flash.swf.tools.SyntaxTreeDumper.dump(programNode, "/tmp/" + docInfo.getClassName() + "-interface.new.xml");

        As3Compiler.cleanNodeFactory(nodeFactory);
    }

    private Set<String> createInterfaceNames()
    {
        Set<String> result = docInfo.getInterfaceNames().stream().map(DocumentInfo.NameInfo::getName).collect(Collectors.toCollection(TreeSet::new));

        return result;
    }

    private StatementListNode generateBindingManagementVars(StatementListNode statementList)
    {
        StatementListNode result = statementList;
        Iterator<VariableDeclaration> iterator = FrameworkDefs.bindingManagementVars.iterator();
        int kind = Tokens.VAR_TOKEN;

        while (iterator.hasNext())
        {
            VariableDeclaration variableDeclaration = iterator.next();
            QualifiedIdentifierNode qualifiedIdentifier =
                AbstractSyntaxTreeUtil.generateMxInternalQualifiedIdentifier(nodeFactory,
                                                                             variableDeclaration.getName(),
                                                                             false);
            VariableDefinitionNode variableDefinition =
                AbstractSyntaxTreeUtil.generateVariable(nodeFactory,
                                                        generateMxInternalAttribute(),
                                                        qualifiedIdentifier,
                                                        variableDeclaration.getType(),
                                                        false, null);
            result = nodeFactory.statementList(result, variableDefinition);
        }

        return result;
    }

    private ClassDefinitionNode generateClassDefinition()
    {
        StatementListNode statementList = null;
        String className = docInfo.getClassName();
        FunctionDefinitionNode constructor =
            AbstractSyntaxTreeUtil.generateConstructor(context, className, null, false, null, -1);
        statementList = nodeFactory.statementList(statementList, constructor);

        statementList = generateInstanceVariables(statementList);
        statementList = generateBindingManagementVars(statementList);
        statementList = generateScripts(statementList, docInfo.getScripts());

        return AbstractSyntaxTreeUtil.generateClassDefinition(context, className,
                                                              docInfo.getQualifiedSuperClassName(),
                                                              createInterfaceNames(), statementList);
    }

    private StatementListNode generateImports(StatementListNode statementList, Set<String> bogusImports)
    {
        StatementListNode result = statementList;

        for (String[] splitImport : docInfo.getSplitImportNames()) {
            ImportDirectiveNode importDirective = AbstractSyntaxTreeUtil.generateImport(context, splitImport);
            result = nodeFactory.statementList(result, importDirective);
        }

        for (DocumentInfo.NameInfo nameInfo : docInfo.getImportNames()) {
            String name = nameInfo.getName();
            ImportDirectiveNode importDirective = AbstractSyntaxTreeUtil.generateImport(context, name);
            result = nodeFactory.statementList(result, importDirective);
        }

        for (String name : bogusImports) {
            ImportDirectiveNode importDirective = AbstractSyntaxTreeUtil.generateImport(context, name);
            result = nodeFactory.statementList(result, importDirective);
        }

        return result;
    }

    private StatementListNode generateInstanceVariables(StatementListNode statementList)
    {
        StatementListNode result = statementList;

        for (DocumentInfo.VarDecl varDecl1 : docInfo.getVarDecls().values()) {
            MetaDataNode bindableMetaData =
                    AbstractSyntaxTreeUtil.generateMetaData(nodeFactory, BINDABLE);
            result = nodeFactory.statementList(result, bindableMetaData);

            DocumentInfo.VarDecl varDecl = varDecl1;

            int position = AbstractSyntaxTreeUtil.lineNumberToPosition(nodeFactory, varDecl.line);

            TypeExpressionNode typeExpression =
                    AbstractSyntaxTreeUtil.generateTypeExpression(nodeFactory, varDecl.className,
                            true, position);
            Node variableDefinition =
                    AbstractSyntaxTreeUtil.generatePublicVariable(context, typeExpression, varDecl.name);
            result = nodeFactory.statementList(result, variableDefinition);
        }

        return result;
    }

    String getPath()
    {
        return docInfo.getPath();
    }
}
