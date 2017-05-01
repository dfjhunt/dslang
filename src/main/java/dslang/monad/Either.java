
package dslang.monad;

import java.util.Objects;
import java.util.function.Function;

import dslang.functor.BiFunctor;
import dslang.functor.SplitFunctor;

/**
 * 
 * @author dahunt
 *
 * @param <A> - left type
 * @param <B> - right type
 */
public class Either<A, B> implements Monad<Either<?, ?>, B>, BiFunctor<A, B>, SplitFunctor<A, B> {
    final A _a;

    final B _b;

    final boolean _isLeft;

    private Either(A a, B b, boolean isLeft) {
        this._a = a;
        this._b = b;
        this._isLeft = isLeft;
    }

    /**
     * Creates a Left Either
     * 
     * @param s - value of the left type
     * @return
     */
    static public <S, T> Either<S, T> left(S s) {
        return new Either<>(s, null, true);
    }

    /**
     * Creates a Right Either
     * 
     * @param t - value of the right type
     * @return
     */
    static public <S, T> Either<S, T> right(T t) {
        return new Either<>(null, t, false);
    }

    /**
     * Is this Either of type Left
     * 
     * @return
     */
    public boolean isLeft() {
        return _isLeft;
    }

    /*
     * (non-Javadoc)
     * 
     * @see dslang.monad.Monad#unit(java.lang.Object)
     */
    @Override
    public <U> Monad<Either<?, ?>, U> unit(U u) {
        return right(u);
    }

    /*
     * (non-Javadoc)
     * 
     * @see dslang.monad.Monad#map(java.util.function.Function)
     */
    @Override
    public <U> Monad<Either<?, ?>, U> map(Function<? super B, ? extends U> mapper) {
        if (_isLeft)
            return left(_a);
        else
            return right(mapper.apply(_b));
    }

    /*
     * (non-Javadoc)
     * 
     * @see dslang.monad.Monad#flatMap(java.util.function.Function)
     */
    @Override
    public <U> Monad<Either<?, ?>, U> flatMap(Function<? super B, ? extends Monad<Either<?, ?>, U>> mapper) {
        if (_isLeft)
            return left(_a);
        else
            return mapper.apply(_b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see dslang.functor.BiFunctor#bimap(java.util.function.Function, java.util.function.Function)
     */
    @Override
    public <C, D> BiFunctor<C, D> bimap(Function<? super A, ? extends C> f1, Function<? super B, ? extends D> f2) {
        Objects.requireNonNull(f1);
        Objects.requireNonNull(f2);
        if (_isLeft)
            return left(f1.apply(_a));
        else
            return right(f2.apply(_b));
    }

    /*
     * (non-Javadoc)
     * 
     * @see dslang.functor.SplitFunctor#repair(java.util.function.Function, java.util.function.Function)
     */
    @Override
    public <C> SplitFunctor<A, C> repair(Function<? super A, ? extends C> left, Function<? super B, ? extends C> right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        C c = _isLeft ? left.apply(_a)
            : right.apply(_b);
        return right(c);
    }
}
