package dslang.util.function.checked;

public interface CheckedConsumer<T,E extends Exception> {
    void accept(T t) throws E;
}
