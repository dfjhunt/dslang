
package dslang.functor;

import java.util.function.Function;

public abstract class LazyFunctor<OF, A> implements Functor<LazyFunctor<OF, ?>, A> {

    private LazyFunctor() {

    }

    @Override
    public abstract <B> LazyFunctor<OF, B> map(Function<? super A, ? extends B> mapper);

    /**
     * Creates a LazyFunctor from the passed functor.
     * 
     * @param fa - functor
     * @return
     */
    static public <OF, A> LazyFunctor<OF, A> lift(Functor<OF, A> fa) {
        return new LazyFunctor.Hidden<OF, A, A>(fa, x -> x);
    }

    /**
     * Takes the inner functor out of the lazy functor container.
     * 
     * @return 
     */
    public abstract <R> Functor<OF, A> run();

    
    
    /**
     * Inner class used to hide the dependent type R which is the type of the original functor.  Need to 
     * store it so we can apply the composed mapping function at the end but don't want to burden the
     * user with it.
     *
     * @param <OFI>
     * @param <R>
     * @param <X>
     */
    static class Hidden<OFI, R, X> extends LazyFunctor<OFI, X> {
        private Functor<OFI, R> _FR;

        private Function<R, X> _fr;

        private Hidden(Functor<OFI, R> fa, Function<R, X> func) {
            _FR = fa;
            _fr = func;
        }

        @Override
        public <B> LazyFunctor<OFI, B> map(Function<? super X, ? extends B> mapper) {
            return new Hidden<OFI, R, B>(_FR, _fr.andThen(mapper));
        }

        @Override
        public Functor<OFI, X> run() {
            return _FR.map(_fr);
        }
    }

}
