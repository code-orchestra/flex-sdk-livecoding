package codeOrchestra.tree.visitor;

import macromedia.asc.parser.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton.I.Neverov
 */
public class NodeVisitorFactory {

    private static Map<Class, NodeVisitor> visitors = new HashMap<>();

    static {
        visitors.put(ApplyTypeExprNode.class, new ApplyTypeExprNodeVisitor());
        visitors.put(ArgumentListNode.class, new ArgumentListNodeVisitor());
        visitors.put(AttributeListNode.class, new AttributeListNodeVisitor());
        visitors.put(BinaryClassDefNode.class, new BinaryClassDefNodeVisitor());
        visitors.put(BinaryExpressionNode.class, new BinaryExpressionNodeVisitor());
        visitors.put(BinaryProgramNode.class, new BinaryProgramNodeVisitor());
        visitors.put(BlockNode.class, new BlockNodeVisitor());
        visitors.put(BoxNode.class, new BoxNodeVisitor());
        visitors.put(BreakStatementNode.class, new BreakStatementNodeVisitor());
        visitors.put(CallExpressionNode.class, new CallExpressionNodeVisitor());
        visitors.put(CaseLabelNode.class, new CaseLabelNodeVisitor());
        visitors.put(CatchClauseNode.class, new CatchClauseNodeVisitor());
        visitors.put(ClassDefinitionNode.class, new ClassDefinitionNodeVisitor());
        visitors.put(ClassNameNode.class, new ClassNameNodeVisitor());
        visitors.put(CoerceNode.class, new CoerceNodeVisitor());
        visitors.put(CommentNode.class, new CommentNodeVisitor());
        visitors.put(ConditionalExpressionNode.class, new ConditionalExpressionNodeVisitor());
        visitors.put(ConfigNamespaceDefinitionNode.class, new ConfigNamespaceDefinitionNodeVisitor());
        visitors.put(ContinueStatementNode.class, new ContinueStatementNodeVisitor());
        visitors.put(DefaultXMLNamespaceNode.class, new DefaultXMLNamespaceNodeVisitor());
        visitors.put(DeleteExpressionNode.class, new DeleteExpressionNodeVisitor());
        visitors.put(DoStatementNode.class, new DoStatementNodeVisitor());
        visitors.put(DocCommentNode.class, new DocCommentNodeVisitor());
        visitors.put(EmptyElementNode.class, new EmptyElementNodeVisitor());
        visitors.put(EmptyStatementNode.class, new EmptyStatementNodeVisitor());
        visitors.put(ErrorNode.class, new ErrorNodeVisitor());
        visitors.put(ExpressionStatementNode.class, new ExpressionStatementNodeVisitor());
        visitors.put(FinallyClauseNode.class, new FinallyClauseNodeVisitor());
        visitors.put(ForStatementNode.class, new ForStatementNodeVisitor());
        visitors.put(FunctionCommonNode.class, new FunctionCommonNodeVisitor());
        visitors.put(FunctionDefinitionNode.class, new FunctionDefinitionNodeVisitor());
        visitors.put(FunctionNameNode.class, new FunctionNameNodeVisitor());
        visitors.put(FunctionSignatureNode.class, new FunctionSignatureNodeVisitor());
        visitors.put(GetExpressionNode.class, new GetExpressionNodeVisitor());
        visitors.put(HasNextNode.class, new HasNextNodeVisitor());
        visitors.put(IdentifierErrorNode.class, new IdentifierErrorNodeVisitor());
        visitors.put(IdentifierNode.class, new IdentifierNodeVisitor());
        visitors.put(IfStatementNode.class, new IfStatementNodeVisitor());
        visitors.put(ImportDirectiveNode.class, new ImportDirectiveNodeVisitor());
        visitors.put(ImportNode.class, new ImportNodeVisitor());
        visitors.put(IncludeDirectiveNode.class, new IncludeDirectiveNodeVisitor());
        visitors.put(IncrementNode.class, new IncrementNodeVisitor());
        visitors.put(InheritanceNode.class, new InheritanceNodeVisitor());
        visitors.put(InterfaceDefinitionNode.class, new InterfaceDefinitionNodeVisitor());
        visitors.put(InvokeNode.class, new InvokeNodeVisitor());
        visitors.put(LabeledStatementNode.class, new LabeledStatementNodeVisitor());
        visitors.put(ListErrorNode.class, new ListErrorNodeVisitor());
        visitors.put(ListNode.class, new ListNodeVisitor());
        visitors.put(LiteralArrayNode.class, new LiteralArrayNodeVisitor());
        visitors.put(LiteralBooleanNode.class, new LiteralBooleanNodeVisitor());
        visitors.put(LiteralFieldNode.class, new LiteralFieldNodeVisitor());
        visitors.put(LiteralNullNode.class, new LiteralNullNodeVisitor());
        visitors.put(LiteralNumberNode.class, new LiteralNumberNodeVisitor());
        visitors.put(LiteralObjectNode.class, new LiteralObjectNodeVisitor());
        visitors.put(LiteralRegExpNode.class, new LiteralRegExpNodeVisitor());
        visitors.put(LiteralStringNode.class, new LiteralStringNodeVisitor());
        visitors.put(LiteralVectorNode.class, new LiteralVectorNodeVisitor());
        visitors.put(LiteralXMLNode.class, new LiteralXMLNodeVisitor());
        visitors.put(LoadRegisterNode.class, new LoadRegisterNodeVisitor());
        visitors.put(MemberExpressionNode.class, new MemberExpressionNodeVisitor());
        visitors.put(MetaDataNode.class, new MetaDataNodeVisitor());
        visitors.put(NamespaceDefinitionNode.class, new NamespaceDefinitionNodeVisitor());
        visitors.put(PackageDefinitionNode.class, new PackageDefinitionNodeVisitor());
        visitors.put(PackageIdentifiersNode.class, new PackageIdentifiersNodeVisitor());
        visitors.put(PackageNameNode.class, new PackageNameNodeVisitor());
        visitors.put(ParameterListNode.class, new ParameterListNodeVisitor());
        visitors.put(ParameterNode.class, new ParameterNodeVisitor());
        visitors.put(ParenExpressionNode.class, new ParenExpressionNodeVisitor());
        visitors.put(ParenListExpressionNode.class, new ParenListExpressionNodeVisitor());
        visitors.put(PragmaExpressionNode.class, new PragmaExpressionNodeVisitor());
        visitors.put(PragmaNode.class, new PragmaNodeVisitor());
        visitors.put(ProgramNode.class, new ProgramNodeVisitor());
        visitors.put(QualifiedExpressionNode.class, new QualifiedExpressionNodeVisitor());
        visitors.put(QualifiedIdentifierNode.class, new QualifiedIdentifierNodeVisitor());
        visitors.put(RegisterNode.class, new RegisterNodeVisitor());
        visitors.put(RestExpressionNode.class, new RestExpressionNodeVisitor());
        visitors.put(RestParameterNode.class, new RestParameterNodeVisitor());
        visitors.put(ReturnStatementNode.class, new ReturnStatementNodeVisitor());
        visitors.put(SetExpressionNode.class, new SetExpressionNodeVisitor());
        visitors.put(StatementListNode.class, new StatementListNodeVisitor());
        visitors.put(StoreRegisterNode.class, new StoreRegisterNodeVisitor());
        visitors.put(SuperExpressionNode.class, new SuperExpressionNodeVisitor());
        visitors.put(SuperStatementNode.class, new SuperStatementNodeVisitor());
        visitors.put(SwitchStatementNode.class, new SwitchStatementNodeVisitor());
        visitors.put(ThisExpressionNode.class, new ThisExpressionNodeVisitor());
        visitors.put(ThrowStatementNode.class, new ThrowStatementNodeVisitor());
        visitors.put(ToObjectNode.class, new ToObjectNodeVisitor());
        visitors.put(TryStatementNode.class, new TryStatementNodeVisitor());
        visitors.put(TypeExpressionNode.class, new TypeExpressionNodeVisitor());
        visitors.put(TypeIdentifierNode.class, new TypeIdentifierNodeVisitor());
        visitors.put(TypedIdentifierNode.class, new TypedIdentifierNodeVisitor());
        visitors.put(UnaryExpressionNode.class, new UnaryExpressionNodeVisitor());
        visitors.put(UntypedVariableBindingNode.class, new UntypedVariableBindingNodeVisitor());
        visitors.put(UseDirectiveNode.class, new UseDirectiveNodeVisitor());
        visitors.put(UseNumericNode.class, new UseNumericNodeVisitor());
        visitors.put(UsePragmaNode.class, new UsePragmaNodeVisitor());
        visitors.put(UsePrecisionNode.class, new UsePrecisionNodeVisitor());
        visitors.put(UseRoundingNode.class, new UseRoundingNodeVisitor());
        visitors.put(VariableBindingNode.class, new VariableBindingNodeVisitor());
        visitors.put(VariableDefinitionNode.class, new VariableDefinitionNodeVisitor());
        visitors.put(WhileStatementNode.class, new WhileStatementNodeVisitor());
        visitors.put(WithStatementNode.class, new WithStatementNodeVisitor());
    }

    public static <T extends Node> NodeVisitor<T> getVisitor(Class<T> nodeClass) {
        NodeVisitor nodeVisitor = visitors.get(nodeClass);
        if (nodeVisitor == null) {
            throw new RuntimeException();
        }
        return nodeVisitor;
    }
}
