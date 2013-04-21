package codeOrchestra.util;

/**
 * @author: Alexander Eliseyev
 */
public class StringUtils {

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
