package dslang.monad.transformer;

import java.util.function.Function;

import dslang.monad.Monad;
import dslang.monad.MonadT;
import dslang.monad.wrapper.FutureM;
import dslang.monad.wrapper.OptionM;

public class FutureT<M,T> implements MonadT<FutureT<M,?>, M, T, OptionM<?>>{

    Monad<FutureM.t, Monad<M, FutureM<T>>> myMonad = null;
    private <A> Monad<M, A> mUnit(A a){
        return null;
    }
    
    private FutureT(Monad<FutureM.t, Monad<M, FutureM<T>>> m){
        myMonad = m;
    }
    
    @Override
    public <U> Monad<FutureT<M, ?>, U> unit(U u) {
        return new FutureT<>(FutureM.sunit(mUnit(FutureM.sunit(u))));
    }
    
    public <U> Monad<FutureT<M,?>, U> map(Function<? super T, ? extends U> mapper){
        return new FutureT<>(myMonad.map(x->x.map(f-> f.map(mapper))));
    }

    @Override
    public <U> Monad<FutureT<M, ?>, U> flatMap(Function<? super T, ? extends Monad<FutureT<M, ?>, U>> mapper) {
        return null;
    }

    @Override
    public Monad<M, Monad<OptionM<?>, T>> lift() {
        return null;
    }

}
