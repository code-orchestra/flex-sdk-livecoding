package codeOrchestra.util;

/**
 * @author Alexander Eliseyev
 */
public class ObjectUtils {

    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        return !((object1 == null) || (object2 == null)) && object1.equals(object2);
    }

}
