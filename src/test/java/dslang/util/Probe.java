package dslang.util;

public class Probe<X> {
    X value;

    public Probe() {
    }

    public Probe(X defVal) {
        value = defVal;
    }

    public X set(X x) {
        value = x;
        return x;
    }

    public X get() {
        return value;
    }
}
