package dslang.monad.transformer;

import java.util.function.Function;

import dslang.monad.Monad;
import dslang.monad.MonadT;
import dslang.monad.Try;


public class TryT<M, T> implements MonadT<TryT<M, ?>, M, T, Try.t>{

    Monad<M, Try<T>> myMonad = null;

    //
    public TryT(Monad<M, Try<T>> monad) {
        myMonad = monad;
    }

    //Just a static constructor
    public static <N, S> TryT<N, S> of(Monad<N, S> m){
        return new TryT<N,S>(m.map(t->Try.of(t)));
    }
    
    @Override
    public <U> TryT<M,U> unit(U u) {
        return new TryT<M, U>(myMonad.unit(Try.of(u)));
    }
    
    public Monad<M, Try<T>> run() {
        return myMonad;
    }
    
    public Monad<M, Monad<Try.t, T>> lift(){
        return myMonad.map(x->(Monad<Try.t, T>)x);
    }
    
    public M liftM(){
        return myMonad.getM();
    }
    
    @SuppressWarnings("unchecked")
    public <U,X> Monad<M,U> lift(Function<? super Try<X>, ? extends U> mapper) {
        Function<Try<T>, Try<X>> uncheckedFix = t->(Try<X>)t; 
        return myMonad.map(mapper.compose(uncheckedFix));
    }

    //TODO - catch exceptions
    @Override
    public <U> TryT<M, U> map(Function<? super T, ? extends U> mapper) {
        Monad<M, Try<U>> t = myMonad.map(o -> o.map(mapper));
        return new TryT<M, U>(t);
    } 
    
    private <U> Monad<M, Try<U>> mapTryToM(Function<? super T, Monad<M, Try<U>>> mapper, Try<T> t) {
        Function<Exception, Monad<M,Try<U>>> forException = e->myMonad.unit(Try.exception(e));
        
        if (!t.isException()) {
            try {
                return mapper.apply(t.get());
            } catch (Exception e) {
                return forException.apply(e);
            }
        } else {
            return forException.apply(t.getException());
        }
    }
    
    @Override
    public <U> TryT<M, U> flatMap(Function<? super T, ? extends Monad<TryT<M, ?>, U>> mapper) {
        Function<? super T, Monad<M, Try<U>>> interimMapper = x -> {
            return ((TryT<M, U>) mapper.apply(x)).run();
        };
        
        Function<Try<T>, Monad<M, Try<U>>> newMapper = t -> mapTryToM(interimMapper, t);
        
        return new TryT<M, U>(myMonad.flatMap(newMapper));
    }
}
