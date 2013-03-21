package codeOrchestra.tree;

import codeOrchestra.FakeASVirtualFile;
import flex2.compiler.CompilationUnit;
import flex2.compiler.CompilerAPI;
import flex2.compiler.CompilerContext;
import flex2.compiler.Source;
import flex2.compiler.mxml.lang.StandardDefs;
import flex2.compiler.util.QName;
import macromedia.asc.parser.*;
import macromedia.asc.util.Context;
import macromedia.asc.util.ObjectList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ClassCONode extends CONode {

    // Set by user
    public String packageName;
    public String className;
    public final List<MethodCONode> methods = new ArrayList<MethodCONode>();
    public final List<FieldCONode> fields = new ArrayList<FieldCONode>();
    public final List<Node> staticInitializer = new ArrayList<Node>();
    private final List<String[]> imports = new ArrayList<String[]>();
    private Context cx;
//    private StandardDefs standardDefs;

    // Set by current generator
    private Source mySource;
    private ClassDefinitionNode myClass;

    // Misc
    private boolean addedToProject;

    public ClassCONode(String packageName, String className, Context cx/*, StandardDefs standardDefs*/) {
        this.packageName = packageName;
        this.className = className;
        this.cx = cx;
//        this.standardDefs = standardDefs;
    }

    public void addToProject() {
        if (addedToProject) {
            throw new RuntimeException();
        }
        generateTree();
        CompilerAPI.addedSources.add(mySource);
        addedToProject = true;
    }

    public void addImport(String packageName, String className) {
        imports.add(new String[] {packageName, className});
    }

    @Override
    protected void generateTree() {
        generateSelf();
        for (Node node : staticInitializer) {
            myClass.statements.items.add(node);
        }
        for (FieldCONode fieldCONode : fields) {
            fieldCONode.packageDefinitionNode = myClass.pkgdef;
            fieldCONode.generateTree();
            myClass.statements.items.add(fieldCONode.variableDefinitionNode);
        }
        for (MethodCONode methodCONode : methods) {
            methodCONode.packageDefinitionNode = myClass.pkgdef;
            methodCONode.generateTree();
            myClass.statements.items.add(methodCONode.functionDefinitionNode);
        }
    }

    private void generateSelf() {
        // Compilation unit
        mySource = new Source(new FakeASVirtualFile(className), packageName, className, null, false, false);
        CompilerContext compilerContext = new CompilerContext();
        compilerContext.setAscContext(cx);
        CompilationUnit compilationUnit = mySource.newCompilationUnit(new ProgramNode(cx, new StatementListNode(null)), compilerContext);
        compilationUnit.setParsedState();
        compilationUnit.topLevelDefinitions.add(new QName(packageName + ":" + className));
        compilationUnit.inheritance.add(new QName("Object"));
//        compilationUnit.setStandardDefs(standardDefs);

        // Package
        PackageNameNode packageNameNode = TreeUtil.createPackageNameNode(packageName);
        StatementListNode statementListNode = new StatementListNode(null);
        PackageDefinitionNode packageDefinitionNode = new PackageDefinitionNode(cx, null, packageNameNode, statementListNode);
        ((ProgramNode) compilationUnit.getSyntaxTree()).pkgdefs.add(packageDefinitionNode);

        // Class
        AttributeListNode attrs = new AttributeListNode(TreeUtil.createPublicModifier(), -1);
        StatementListNode classStatements = new StatementListNode(null);
        myClass = new ClassDefinitionNode(
                cx,
                packageDefinitionNode,
                attrs,
                new QualifiedIdentifierNode(attrs, className, -1),
                null,
                null,
                classStatements
        );
        myClass.clsdefs = new ObjectList<ClassDefinitionNode>();
        statementListNode.items.add(myClass);
        ((ProgramNode) compilationUnit.getSyntaxTree()).statements.items.add(myClass);

        // Imports
        for (String[] anImport : imports) {
            TreeUtil.addImport(compilationUnit, anImport[0], anImport[1]);
        }
    }

}
