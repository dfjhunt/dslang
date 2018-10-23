package dslang.temp.feh;

import dslang.temp.simple.Wrap;

public interface ExprWBool<T> extends MultExpr<Wrap<T, Integer>> {
    public Wrap<T, Boolean> bool(boolean t);
    public Wrap<T, Boolean> and(Wrap<T,Boolean> t1, Wrap<T,Boolean> t2);
}
