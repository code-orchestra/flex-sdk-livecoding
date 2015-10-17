package codeOrchestra.util;

/**
 * @author Alexander Eliseyev
 */
public class Pair<T,S extends Object> {

    private T o1;
    private S o2;

    public Pair(T o1, S o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public T getO1() {
        return o1;
    }

    public S getO2() {
        return o2;
    }
}
