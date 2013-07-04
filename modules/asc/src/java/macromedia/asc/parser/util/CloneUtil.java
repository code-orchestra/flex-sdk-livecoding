package macromedia.asc.parser.util;

import macromedia.asc.semantics.ObjectValue;
import macromedia.asc.semantics.ReferenceValue;
import macromedia.asc.util.Namespaces;
import macromedia.asc.util.ObjectList;

/**
 * Created with IntelliJ IDEA.
 * User: dimakruk
 * Date: 7/4/13
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class CloneUtil {

    public static Namespaces cloneList(Namespaces list)
    {
        Namespaces clone = new Namespaces(list.size());
        for (ObjectValue item: list) clone.add(item.copyObject());
        return clone;
    }

    public static ObjectList<ReferenceValue> cloneList(ObjectList<ReferenceValue> list) throws CloneNotSupportedException
    {
        ObjectList<ReferenceValue> clone = new ObjectList<ReferenceValue>(list.size());
        for (ReferenceValue item: list) clone.add(item.clone());
        return clone;
    }
}
