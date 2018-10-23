
package dslang.monad.transformer;

import java.util.function.Function;

import dslang.monad.Monad;
import dslang.monad.MonadT;
import dslang.monad.Reader;
import dslang.monad.Reader.t;
import dslang.monad.wrapper.OptionM;

public class ReaderT<E, M, T> implements MonadT<ReaderT<E, M, ?>, M, T, Reader.t<E>> {

    Monad<M, Reader<E, T>> myMonad = null;

    Monad<M, Reader<E, ReaderT<E, M, T>>> myNestedMonad = null;

    private ReaderT() {

    }

    public ReaderT(Monad<M, Reader<E, T>> monad) {
        myMonad = monad;
    }

    private static <I, N, A> ReaderT<I, N, A> nest(Monad<N, Reader<I, ReaderT<I, N, A>>> nestedMonad) {
        ReaderT<I, N, A> readerT = new ReaderT<>();
        readerT.myNestedMonad = nestedMonad;
        return readerT;
    }

    @Override
    public <U> Monad<ReaderT<E, M, ?>, U> unit(U u) {
        return new ReaderT<>(myMonad.unit(Reader.<E, U>of(x -> u)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Monad<ReaderT<E, M, ?>, U> flatMap(Function<? super T, ? extends Monad<ReaderT<E, M, ?>, U>> mapper) {
        Function<T, ReaderT<E, M, U>> temp = t -> (ReaderT<E, M, U>) mapper.apply(t);

        if (myMonad != null) {
            return ReaderT.<E, M, U>nest(myMonad.map(r -> r.map(temp)));
        } else {
            return ReaderT.<E, M, U>nest(myNestedMonad.map(r -> r.map(rt -> (ReaderT<E, M, U>) rt.flatMap(temp))));
        }
    }

    @Override
    public Monad<M, Monad<t<E>, T>> lift() {
        return null;// (Monad<M, Monad<t<E>, T>>)myMonad;
    }

    public Monad<M, T> runReader(E e) {
        if (myMonad != null) {
            return myMonad.map(r -> r.run(e));
        } else {
            return myNestedMonad.flatMap(r -> r.run(e).runReader(e));
        }
    }

    public static void main(String args[]){
        OptionM<Reader<Integer, Integer>> opt = OptionM.of(Reader.of(x->x+1));
        
        ReaderT<Integer, OptionM.t, Integer> temp = new ReaderT<>(opt);
        
        System.out.println(temp.runReader(3));
        
        @SuppressWarnings("unchecked")
        ReaderT<Integer, OptionM.t, String> temp2 = (ReaderT<Integer, OptionM.t, String>)temp.<String>flatMap(x->new ReaderT<>(OptionM.of(Reader.of(y->""+x+"A"+y))));
        
        System.out.println(temp2.runReader(3));
        
        @SuppressWarnings("unchecked")
        ReaderT<Integer, OptionM.t, String> temp3 = (ReaderT<Integer, OptionM.t, String>)temp2.<String>flatMap(x->new ReaderT<>(OptionM.of(Reader.of(y->""+x+"B"+y))));
    
        System.out.println(temp3.runReader(3));
    }
    
}
