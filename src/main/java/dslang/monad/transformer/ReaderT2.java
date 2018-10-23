
package dslang.monad.transformer;

import java.util.function.Function;

import dslang.monad.Monad;
import dslang.monad.MonadT;
import dslang.monad.Reader;
import dslang.monad.Reader.t;
import dslang.monad.wrapper.OptionM;

public class ReaderT2<E, M, T> implements MonadT<ReaderT2<E, M, ?>, M, T, Reader.t<E>> {

    private Monad<M, Monad<Reader.t<E>, Monad<M, T>>> myMonad = null;

    private ReaderT2() {

    }

    public ReaderT2(Monad<M, Reader<E, T>> monad) {
        myMonad = monad.map(r -> r.map(monad::unit));
    }

    private static <I, N, A> ReaderT2<I, N, A> nest(Monad<N, Monad<Reader.t<I>, Monad<N, A>>> nestedMonad) {
        ReaderT2<I, N, A> readerT = new ReaderT2<>();
        readerT.myMonad = nestedMonad;
        return readerT;
    }

    @Override
    public <U> Monad<ReaderT2<E, M, ?>, U> unit(U u) {
        return new ReaderT2<>(myMonad.unit(Reader.<E, U>of(x -> u)));
    }

   public static <I> ReaderTBuilder<I, I> ask() {
        return new ReaderTBuilder<>(Reader.ask());
    }

    public static <I, A> ReaderTBuilder<I, A> of(Function<I, A> f) {
        return new ReaderTBuilder<>(Reader.of(f));
    }

    static class ReaderTBuilder<I,A> {
        Reader<I, A> reader;

        public ReaderTBuilder(Reader<I, A> reader) {
            this.reader = reader;
        }

        public <M> ReaderT2<I, M, A> forM(Function<Reader<I, A>, Monad<M, Reader<I, A>>> of2) {
            return new ReaderT2<>(of2.apply(reader));
        }
    }

    //R<M<R<M>>> -> R<M>
    private <I, N, A> Monad<Reader.t<I>, Monad<N, A>> convert(Monad<Reader.t<I>, Monad<N, Monad<Reader.t<I>, Monad<N, A>>>> from) {
        Reader<I, Monad<N, Reader<I, Monad<N, A>>>> fromFixed =
            (Reader<I, Monad<N, Reader<I, Monad<N, A>>>>) from.map(n -> n.map(r -> (Reader<I, Monad<N, A>>) r));
        return new Reader<>(i -> fromFixed.run(i).flatMap(r -> r.run(i)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Monad<ReaderT2<E, M, ?>, U> flatMap(Function<? super T, ? extends Monad<ReaderT2<E, M, ?>, U>> mapper) {
        Function<T, Monad<M, Monad<Reader.t<E>, Monad<M, U>>>> temp = t -> ((ReaderT2<E, M, U>) mapper.apply(t)).myMonad;

        //M<R<M<R<M<U>>>>>
        Monad<M, Monad<Reader.t<E>, Monad<M, Monad<Reader.t<E>, Monad<M, U>>>>> temp2 = myMonad.map(r -> r.map(o -> o.flatMap(temp)));

        Monad<M, Monad<Reader.t<E>, Monad<M, U>>> temp3 = temp2.map(n -> convert(n));

        return (Monad<ReaderT2<E, M, ?>, U>) nest(temp3);

    }

    @Override
    public Monad<M, Monad<t<E>, T>> lift() {
        return null;
    }

    public Monad<M, T> runReader(E e) {
        return myMonad.flatMap(r -> ((Reader<E, Monad<M, T>>) r).run(e));

    }

    @SuppressWarnings("unchecked")
    public static <I, N, A> ReaderT2<I, N, A> wrap(Monad<ReaderT2<I, N, ?>, A> monad) {
        return (ReaderT2<I, N, A>) monad;
    }

    public static void main(String args[]) {
        ReaderT2<Integer, OptionM.t, Integer> temp = ReaderT2.of((Integer x)->x+1).forM(OptionM::of);

        System.out.println(temp.runReader(3));

        ReaderT2<Integer, OptionM.t, String> temp2 =
            ReaderT2.wrap(temp.flatMap(x -> ReaderT2.<Integer>ask().forM(OptionM::of).map(y -> "" + x + "A" + y)));

        System.out.println(temp2.runReader(2));

        ReaderT2<Integer, OptionM.t, String> temp3 =
            ReaderT2.wrap(temp2.flatMap(x -> ReaderT2.of((Integer y) -> "" + x + "B" + y).forM(OptionM::of)));

        System.out.println(temp3.runReader(1));
    }

}
