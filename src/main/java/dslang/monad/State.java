package dslang.monad;

import java.util.function.Function;

import dslang.util.Pair;

public class State<S, A> implements Monad<State.t<S>, A> {
    public static class t<S>{
    }
    
    Function<S, Pair<A, S>> run;

    public State(Function<S, Pair<A, S>> run) {
        this.run = run;
    }

    public Pair<A, S> run(S s) {
        return run.apply(s);
    }

    public static <T> State<T, T> get() {
        return new State<>(s -> Pair.of(s, s));
    }

    public static <T> State<T, Void> set(T s) {
        return new State<>(x -> Pair.of(null, s));
    }

    public static <T> State<T, Void> modify(Function<T, T> f) {
        return For.of(State.<T> get(), // x
                      s -> State.set(f.apply(s)), // y
                      (x, y) -> null);
    }

    @Override
    public <U> State<S, U> unit(U u) {
        return sunit(u);
    }

    public static <T, U> State<T, U> sunit(U u) {
        return new State<>(s -> Pair.of(u, s));
    }

    @Override
    public <U> State<S, U> map(Function<? super A, ? extends U> mapper) {
        return new State<>(s -> run.apply(s).first((a -> mapper.apply(a))));
    }

    @Override
    public <U> State<S, U> flatMap(Function<? super A, ? extends Monad<State.t<S>, U>> mapper) {
        Function<Pair<A, S>, Pair<U, S>> f = p -> p.mapPair((a, s) -> ((State<S, U>) mapper.apply(a)).run(s));
        return new State<>(s -> f.apply(run.apply(s)));
    }

}
