
package dslang.monad.transformer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import dslang.monad.Monad;
import dslang.monad.MonadT;
import dslang.monad.wrapper.FutureM;
import dslang.monad.wrapper.ListM;
import dslang.monad.wrapper.OptionM;
import dslang.monad.wrapper.StreamM;
import dslang.util.function.Fluent;

public class OptionT<M, T> implements MonadT<OptionT<M, ?>, M, T, OptionM<?>> {

    Monad<M, OptionM<T>> myMonad = null;

    public OptionT(Monad<M, OptionM<T>> monad) {
        myMonad = monad;
    }

    @SuppressWarnings("unchecked")
    public <N extends Monad<M, OptionM<T>>> N run() {
        return (N) myMonad;
    }

    private Monad<M, Monad<OptionM<?>, T>> convert(Monad<M, OptionM<T>> monad){
        return monad.map(x->(Monad<OptionM<?>, T>)x);
    }
    
    @Override
    public Monad<M, Monad<OptionM<?>, T>> lift() {
        // TODO Auto-generated method stub
        return convert(myMonad);
    }
    
    public M liftM(){
        return myMonad.getM();
    }
    
    // The point of this is to call map on the nested monad, directly rather than mapping it into the nested OptionM.
    // So the function should take an OptionM and return some value.
    @SuppressWarnings("unchecked")
    public <U, X, N extends Monad<M,U>> N lift(Function<? super OptionM<X>, ? extends U> mapper) {
        Function<OptionM<T>, OptionM<X>> uncheckedFix = t -> (OptionM<X>) t;
        return (N)myMonad.map(mapper.compose(uncheckedFix));
    }

    public static <N, S> OptionT<N, S> of(Monad<N, S> m) {
        return new OptionT<N, S>(m.map(t -> OptionM.sunit(t)));
    }

    @Override
    public <U> Monad<OptionT<M, ?>, U> unit(U u) {
        return new OptionT<M, U>(myMonad.unit(OptionM.sunit(u)));
    }

    @Override
    public <U> OptionT<M, U> map(Function<? super T, ? extends U> mapper) {
        Monad<M, OptionM<U>> t = myMonad.map(o -> o.map(mapper));
        return new OptionT<M, U>(t);
    }

    @Override
    public <U> OptionT<M, U> flatMap(Function<? super T, ? extends Monad<OptionT<M, ?>, U>> mapper) {
        // The OptionT constructor expects Monad<M, OptionM<U>>,
        // this composes a transformation onto the mapper to create that output
        Function<? super T, Monad<M, OptionM<U>>> newMapper = x -> {
            return ((OptionT<M, U>) mapper.apply(x)).run();
        };

        // Our nested Monad contains an OptionM<T> as it's type, so we call flatMap
        // on the nested Monad, in which we take the value out of the OptionM and then run the
        // mapper
        Monad<M, OptionM<U>> m = myMonad.flatMap((OptionM<T> o) -> {
            if (o.unwrap().isPresent()) {
                return newMapper.apply(o.unwrap().get());
            } else {
                return myMonad.unit(OptionM.empty());
            }
        });

        return new OptionT<M, U>(m);
    }

    public static void main(String args[]) {
        Function<Object, Object> println = Fluent.of(System.out::println);

        FutureM<OptionM<Integer>> foi = FutureM.sunit(OptionM.sunit(3));
        
        OptionT<FutureM<?>, Integer> of = new OptionT<FutureM<?>, Integer>(foi);

        OptionT<FutureM<?>, Integer> of2 = of.map(x -> x + 4);

        OptionT<FutureM<?>, Integer> of3 = of2.flatMap(x -> {
            return new OptionT<FutureM<?>, Integer>(FutureM.sunit(OptionM.sunit(x + 13)));
        });

        of3.map(println);

        OptionT<StreamM<?>, Integer> os = OptionT.of(StreamM.of(Stream.of(1, 4)));

        OptionT<StreamM<?>, Integer> os2 = os.map(x -> x + 4);

        os2.map(println).lift().getM().unwrap().forEach(x -> {});

        os2 = OptionT.of(StreamM.of(Stream.of(1, 4)));

        OptionT<StreamM<?>, Integer> os3 = os2.flatMap(x -> {
            Monad<StreamM<?>, OptionM<Integer>> temp = StreamM.of(Stream.of(OptionM.sunit(x + 13)));
            return new OptionT<StreamM<?>, Integer>(temp);
        });

        os3.map(println).lift().getM().unwrap().forEach(x -> {
        });

        System.out.println("\nlast test");

        OptionT<ListM<?>, Integer> optList = OptionT.of(ListM.of(Arrays.asList(1, 2, 3)));
        Function<Integer, List<Integer>> i2Li = x -> Arrays.asList(x, x * 2);
        ListM<OptionM<Integer>> optList2 = optList.flatMap(i2Li.andThen(ListM::of).andThen(OptionT::of)).run();
        List<OptionM<Integer>> l = optList2.unwrap();
        for (OptionM<Integer> o : l) {
            System.out.println(o);
        }
    }

    
}
