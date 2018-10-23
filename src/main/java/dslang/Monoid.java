package dslang;

import java.util.List;

public interface Monoid<M> {
    public M collapse(List<M> l);
}
