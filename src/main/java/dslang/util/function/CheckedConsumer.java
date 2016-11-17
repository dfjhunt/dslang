package dslang.util.function;

public interface CheckedConsumer<T,E extends Exception> {
    void accept(T t) throws E;
}
