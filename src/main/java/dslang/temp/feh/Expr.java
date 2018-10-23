package dslang.temp.feh;

public interface Expr<T> {
    public T num(int i);
    public T add(T t1, T t2);
}
