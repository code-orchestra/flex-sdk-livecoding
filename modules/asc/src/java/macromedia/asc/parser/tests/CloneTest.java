package macromedia.asc.parser.tests;

import junit.framework.*;
import macromedia.asc.parser.Node;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
            };
        }
    }
}