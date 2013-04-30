package codeOrchestra.util;

/**
 * @author Alexander Eliseyev
 */
public class Pair<T extends Object> {

    private T o1;
    private T o2;

    public Pair(T o1, T o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public T getO1() {
        return o1;
    }

    public T getO2() {
        return o2;
    }
}
