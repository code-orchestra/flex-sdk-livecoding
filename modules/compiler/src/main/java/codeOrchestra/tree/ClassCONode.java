package codeOrchestra.tree;

import codeOrchestra.FakeASVirtualFile;
import codeOrchestra.util.StringUtils;
import flex2.compiler.CompilationUnit;
import flex2.compiler.CompilerAPI;
import flex2.compiler.CompilerContext;
import flex2.compiler.Source;
import flex2.compiler.util.QName;
import macromedia.asc.parser.*;
import macromedia.asc.util.Context;
import macromedia.asc.util.ObjectList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 * @author Alexander Eliseyev
 */
public class ClassCONode extends CONode {

    // Set by user
    public boolean isInterface;
    public String packageName;
    public String className;
    public final List<MethodCONode> methods = new ArrayList<>();
    public final List<FieldCONode> fields = new ArrayList<>();
    public final List<Node> staticInitializer = new ArrayList<>();
    public final List<String> interfaces = new ArrayList<>();
    private final List<ImportElement> imports = new ArrayList<>();
    private boolean dynamic;
    private Context cx;

    // Set by current generator
    private Source mySource;
    private ClassDefinitionNode myClass;

    // Misc
    private boolean addedToProject;
    private CompilationUnit compilationUnit;

    public ClassCONode(String packageName, String className, Context cx) {
        this.packageName = packageName;
        this.className = className;
        this.cx = cx;
    }

    public ClassDefinitionNode addToProject() {
        if (addedToProject) {
            throw new RuntimeException();
        }
        generateTree();
        CompilerAPI.addedSources.add(mySource);
        addedToProject = true;

        return myClass;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    public void addImport(String packageName, String className) {
        ImportElement importElement = new ImportElement(packageName, className);
        if (!imports.contains(importElement)) {
            imports.add(importElement);
        }
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public String getFQName() {
        return StringUtils.longNameFromNamespaceAndShortName(packageName, className);
    }

    @Override
    protected void generateTree() {
        compilationUnit = generateSelf();
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

    private CompilationUnit generateSelf() {
        // Compilation unit
        mySource = new Source(new FakeASVirtualFile(className), packageName, className, null, false, false);
        CompilerContext compilerContext = new CompilerContext();
        compilerContext.setAscContext(cx);
        CompilationUnit compilationUnit = mySource.newCompilationUnit(new ProgramNode(cx, new StatementListNode(null)), compilerContext);
        compilationUnit.setParsedState();
        compilationUnit.topLevelDefinitions.add(new QName(packageName + ":" + className));
        compilationUnit.inheritance.add(new QName("Object"));

        // Package
        PackageNameNode packageNameNode = TreeUtil.createPackageNameNode(packageName);
        StatementListNode statementListNode = new StatementListNode(null);
        PackageDefinitionNode packageDefinitionNode = new PackageDefinitionNode(cx, null, packageNameNode, statementListNode);
        ((ProgramNode) compilationUnit.getSyntaxTree()).pkgdefs.add(packageDefinitionNode);

        // Class
        AttributeListNode attrs = new AttributeListNode(TreeUtil.createPublicModifier(), -1);
        if (isDynamic()) {
            attrs.items.add(TreeUtil.createDynamicModifier());
        }
        StatementListNode classStatements = new StatementListNode(null);
        myClass = isInterface
                ? new InterfaceDefinitionNode(
                cx,
                packageDefinitionNode,
                attrs,
                new QualifiedIdentifierNode(attrs, className, -1),
                null,
                classStatements)

                : new ClassDefinitionNode(
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

        // Interfaces
        if (!interfaces.isEmpty()) {
            String firstInterface = interfaces.remove(0);
            myClass.interfaces = new ListNode(null, TreeUtil.createIdentifier(firstInterface), -1);
            for (String anInterface : interfaces) {
                myClass.interfaces.items.add(TreeUtil.createIdentifier(firstInterface));
            }
        }

        // Imports
        for (ImportElement anImport : imports) {
            TreeUtil.addImport(compilationUnit, anImport.packageName, anImport.className);
        }

        cx.addValidImport(packageName + ":" + className);

        return compilationUnit;
    }

    public ClassDefinitionNode getMyClass() {
        return myClass;
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    private static class ImportElement {

        public String packageName;
        public String className;

        private ImportElement(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ImportElement that = (ImportElement) o;

            return !(className != null ? !className.equals(that.className) : that.className != null) && !(packageName != null ? !packageName.equals(that.packageName) : that.packageName != null);

        }

        @Override
        public int hashCode() {
            int result = packageName != null ? packageName.hashCode() : 0;
            result = 31 * result + (className != null ? className.hashCode() : 0);
            return result;
        }
    }

}
