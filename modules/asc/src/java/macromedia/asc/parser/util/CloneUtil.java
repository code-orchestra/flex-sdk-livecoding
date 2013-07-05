package macromedia.asc.parser.util;

import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.parser.FunctionCommonNode;
import macromedia.asc.parser.Node;
import macromedia.asc.parser.ParameterNode;
import macromedia.asc.semantics.*;
import macromedia.asc.util.*;

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

    public static Multinames cloneMultinames(Multinames src) throws CloneNotSupportedException
    {
        Multinames dst = new Multinames();
        for (Map.Entry<String, Namespaces> entry : src.entrySet()) {
            dst.put(entry.getKey(), cloneList(entry.getValue()));
        }
        return dst;
    }

    public static ObjectList<Node> cloneListNode(ObjectList<Node> src) throws CloneNotSupportedException
    {
        ObjectList<Node> dst = new ObjectList<Node>(src.size());
        for (Node item: src) dst.add(item.clone());
        return dst;
    }

    public static ObjectList<String> cloneListString(ObjectList<String> src) throws CloneNotSupportedException
    {
        ObjectList<String> dst = new ObjectList<String>(src.size());
        for (String item: src) dst.add(new String(item));
        return dst;
    }

    public static ObjectList<ClassDefinitionNode> cloneListCDNode(ObjectList<ClassDefinitionNode> src) throws CloneNotSupportedException
    {
        ObjectList<ClassDefinitionNode> dst = new ObjectList<ClassDefinitionNode>(src.size());
        for (ClassDefinitionNode item: src) dst.add(item.clone());
        return dst;
    }

    public static ObjectList<Block> cloneListBlock(ObjectList<Block> src) throws CloneNotSupportedException
    {
        ObjectList<Block> dst = new ObjectList<Block>(src.size());
        for (Block item: src) dst.add(item.clone());
        return dst;
    }

    public static ObjectList<FunctionCommonNode> cloneListFCNode(ObjectList<FunctionCommonNode> src) throws CloneNotSupportedException
    {
        ObjectList<FunctionCommonNode> dst = new ObjectList<FunctionCommonNode>(src.size());
        for (FunctionCommonNode item: src) dst.add(item.clone());
        return dst;
    }

    public static ObjectList<ParameterNode> cloneListPNode(ObjectList<ParameterNode> src) throws CloneNotSupportedException
    {
        ObjectList<ParameterNode> dst = new ObjectList<ParameterNode>(src.size());
        for (ParameterNode item: src) dst.add(item.clone());
        return dst;
    }

    public static ObjectList<TypeInfo> cloneListTypeInfo(ObjectList<TypeInfo> src) throws CloneNotSupportedException
    {
        ObjectList<TypeInfo> dst = new ObjectList<TypeInfo>(src.size());
        for (TypeInfo item: src) dst.add(item.clone());
        return dst;
    }
}
