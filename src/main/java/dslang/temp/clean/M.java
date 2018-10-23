package dslang.temp.clean;

import java.util.function.Function;

public abstract class M<A> {

    public abstract <B> M<B> map(Function<A,B> f);
    
    public abstract <B> M<B> flatMap(Function<A,M<B>> f);
}
