package dslang.temp.clean;

import java.util.Optional;
import java.util.function.Function;

public class OptionalM<A> extends M<A>{

    Optional<A> opt;
    
    public OptionalM(Optional<A> opt){
        this.opt = opt;
    }
    
    public static <X> OptionalM<X> of(X x){
        return new OptionalM<>(Optional.of(x));
    }
    
    @Override
    public <B> M<B> map(Function<A, B> f) {
        return new OptionalM<>(opt.map(f));
    }

    @Override
    public <B> M<B> flatMap(Function<A, M<B>> f) {
        Function<A,Optional<B>> fOpt = a->((OptionalM<B>)f.apply(a)).opt;
        return new OptionalM<>(opt.flatMap(fOpt));
    }

}
