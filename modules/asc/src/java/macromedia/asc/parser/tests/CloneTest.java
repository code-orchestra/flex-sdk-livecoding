package macromedia.asc.parser.tests;

import junit.framework.*;
import macromedia.asc.embedding.LintEvaluator;
import macromedia.asc.parser.*;
import macromedia.asc.semantics.*;
import macromedia.asc.util.ByteList;
import macromedia.asc.util.Context;
import macromedia.asc.util.ContextStatics;

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

        slot.addDeclStyle(42);

        // set various aux data (todo: other data too)
        slot.setEmbeddedData(rec);

        Slot clonedSlot = slot.clone();

        assertFalse(slot == clonedSlot);
        assertEquals(slot, clonedSlot);

        // are DeclStyles properly cloned ?
        slot.getDeclStyles().set(0, (byte) 2);
        assertTrue(clonedSlot.getDeclStyles().at(0)==42);

        // are aux data properly cloned ?
        rec.has_been_declared = false;
        assertTrue(((LintEvaluator.LintDataRecord) clonedSlot.getEmbeddedData()).has_been_declared);

        // test VariableSlot
        slot = new VariableSlot((TypeValue)null, 1, 2);
        slot.setTypeRef(new ReferenceValue(new Context(new ContextStatics()), null, "name", (ObjectValue)null));
        slot.getTypeRef().setPosition(42);

        clonedSlot = slot.clone();

        assertFalse(slot == clonedSlot);
        assertEquals(slot, clonedSlot);

        assertTrue(clonedSlot.getTypeRef().getPosition() == 42);

        // is ReferenceValue properly cloned?
        // todo: test more ReferenceValue stuff here
        slot.getTypeRef().setPosition(69);
        assertFalse(clonedSlot.getTypeRef().getPosition() == 69);

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