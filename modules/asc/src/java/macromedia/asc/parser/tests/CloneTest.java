package macromedia.asc.parser.tests;

import junit.framework.*;
import macromedia.asc.parser.*;
import macromedia.asc.util.DoubleNumberConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static macromedia.asc.semantics.Slot.PARAM_Required;

/**
 * Created with IntelliJ IDEA.
 * User: dimakruk
 * Date: 7/1/13
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class CloneTest extends TestCase{


    public void testNode() throws Exception {
        Node node = new Node();
        testCloneNode(node);

        ParenListExpressionNode pleNode = new ParenListExpressionNode(node);
        testCloneNode(pleNode);

        ReturnStatementNode rsNode = new ReturnStatementNode(node);
        testCloneNode(rsNode);

        ListNode lNode = new ListNode(null, node, 0);
        testCloneNode(lNode);

        PragmaNode pNode = new PragmaNode(lNode);
        testCloneNode(pNode);

        ArgumentListNode alNode = new ArgumentListNode(node, 0);
        testCloneNode(alNode);
        alNode.addDeclStyle(PARAM_Required);
        testCloneNode(alNode);

        LiteralNumberNode lnNode = new LiteralNumberNode("1");
        testCloneNode(lnNode);
        lnNode.numericValue = new DoubleNumberConstant(1000);
        testCloneNode(lnNode);
    }

    public void testCloneNode(Node node) throws Exception {
        Node clonedNode = node.clone();

        assertFalse(node == clonedNode);
        assertEquals(node, clonedNode);

        Class c = node.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields)
        {
            if(!Modifier.isStatic(field.getModifiers()))
            {
                field.setAccessible(true);
                assertEquals(field.get(node), field.get(clonedNode));
                if (field.get(node) != null && field.getType().getName() != "java.lang.String")
                {
                    assertFalse(field.get(node) == field.get(clonedNode));
                }
            };
        }
    }
}