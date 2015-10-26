package codeOrchestra.util;

/**
 * @author Alexander Eliseyev
 */
public class Triple<T,S,P> {

    private T o1;
    private S o2;
    private P o3;

    public Triple(T o1, S o2, P o3) {
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
    }

    public T getO1() {
        return o1;
    }

    public S getO2() {
        return o2;
    }

    public P getO3() {
        return o3;
    }

}