package dslang.temp.simple;

public interface Expr<T> {
    public Wrap<T, Integer> number(int n);
    public Wrap<T, Integer> add(Wrap<T, Integer> n1, Wrap<T, Integer> n2);
    public Wrap<T, Integer> increment(Wrap<T, Integer> n);
}
