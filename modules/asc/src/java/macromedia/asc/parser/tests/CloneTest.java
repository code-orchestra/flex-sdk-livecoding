package macromedia.asc.parser.tests;

import junit.framework.*;
import macromedia.asc.embedding.LintEvaluator;
import macromedia.asc.parser.*;
import macromedia.asc.semantics.MethodSlot;
import macromedia.asc.semantics.Slot;
import macromedia.asc.semantics.TypeValue;

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
    }

    public void testSlot() throws Exception {
        LintEvaluator.LintDataRecord rec = new LintEvaluator.LintDataRecord();
        rec.has_been_declared = true;

        Slot slot = new MethodSlot((TypeValue)null, 0);

        // set various aux data
        slot.setEmbeddedData(rec);

        Slot clonedSlot = slot.clone();

        assertFalse(slot == clonedSlot);
        assertEquals(slot, clonedSlot);

        assertTrue(((LintEvaluator.LintDataRecord)clonedSlot.getEmbeddedData()).has_been_declared);
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
                if (field.get(node) != null)
                {
                    assertFalse(field.get(node) == field.get(clonedNode));
                }
            }
        }
    }
}