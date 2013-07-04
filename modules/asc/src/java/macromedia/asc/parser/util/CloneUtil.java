package macromedia.asc.parser.util;

import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.semantics.ObjectValue;
import macromedia.asc.semantics.ReferenceValue;
import macromedia.asc.semantics.Slot;
import macromedia.asc.semantics.TypeValue;
import macromedia.asc.util.Namespaces;
import macromedia.asc.util.ObjectList;
import macromedia.asc.util.Slots;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dimakruk
 * Date: 7/4/13
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class CloneUtil {

    public static Namespaces cloneList(Namespaces list) throws CloneNotSupportedException
    {
        Namespaces clone = new Namespaces(list.size());
        for (ObjectValue item: list) clone.add(item != null ? item.clone() : null);
        return clone;
    }

    public static ObjectList<ReferenceValue> cloneReferenceValueList(ObjectList<ReferenceValue> list) throws CloneNotSupportedException
    {
        ObjectList<ReferenceValue> clone = new ObjectList<ReferenceValue>(list.size());
        for (ReferenceValue item: list) clone.add(item.clone());
        return clone;
    }

    public static Slots cloneSlotList(Slots list) throws CloneNotSupportedException
    {
        Slots clone = new Slots();
        for (Slot item: list) clone.put(item.clone());
        return clone;
    }

    public static ObjectList<ObjectValue> cloneObjectListList(ObjectList<ObjectValue> list) throws CloneNotSupportedException
    {
        ObjectList<ObjectValue> clone = new ObjectList<ObjectValue>(list.size());
        for (ObjectValue item: list) clone.add(item.clone());
        return clone;
    }

    public  static HashMap<TypeValue,ClassDefinitionNode> cloneHashMap(HashMap<TypeValue,ClassDefinitionNode> src) throws CloneNotSupportedException
    {
        HashMap<TypeValue, ClassDefinitionNode> dst = new HashMap<TypeValue, ClassDefinitionNode>();
        for (Map.Entry<TypeValue, ClassDefinitionNode> entry : src.entrySet()) {
            dst.put(entry.getKey().clone(), entry.getValue().clone());
        }
        return dst;
    }

    public static Map<String, TypeValue> cloneMapS_TV(Map<String, TypeValue> src) throws CloneNotSupportedException
    {
        Map<String, TypeValue> dst = new HashMap<String, TypeValue> ();
        for (Map.Entry<String, TypeValue> entry : src.entrySet()) {
            dst.put(new String(entry.getKey()), entry.getValue().clone());
        }
        return dst;
    }
}
