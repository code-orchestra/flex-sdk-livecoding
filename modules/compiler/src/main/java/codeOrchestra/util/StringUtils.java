package codeOrchestra.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 * @author Alexander Eliseyev
 */
public class StringUtils {

    public static String generateId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String concatenate(String str, int number) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < number; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String join(String[] array, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String joinCollection(Collection array, String separator) {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = array.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String namespaceFromLongName(String fqName) {
        if (fqName == null) {
            return fqName;
        }
        int offset = fqName.lastIndexOf('.');
        if (offset < 0) {
            return "";
        }
        return fqName.substring(0, offset);
    }

    public static String longNameFromNamespaceAndShortName(String namespace, String name) {
        if (isEmpty(namespace)) {
            return name;
        }
        return namespace + '.' + name;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String shortNameFromLongName(String fqName) {
        if (fqName == null) return fqName;
        int offset = fqName.lastIndexOf('.');
        if (offset < 0) return fqName;

        return fqName.substring(offset + 1);
    }


}
