package dslang.monad;

import java.util.function.Function;

import dslang.functor.BiFunctor;
import dslang.functor.SplitFunctor;

public class Either<A, B> implements Monad<Either<?, ?>, B>, BiFunctor<Either<?,?>, A, B> , SplitFunctor<Either<?,?>, A, B>{
	final A _a;
	final B _b;

	final boolean _isLeft;

	protected Either(A a, B b, boolean isLeft) {
		this._a = a;
		this._b = b;
		this._isLeft = isLeft;
	}

	static public <S, T> Either<S, T> left(S s) {
		return new Either<>(s, null, true);
	}

	static public <S, T> Either<S, T> right(T t) {
		return new Either<>(null, t, false);
	}

	public boolean isLeft() {
		return _isLeft;
	}

	@Override
	public <U> Monad<Either<?, ?>, U> unit(U u) {
		return right(u);
	}

	@Override
	public <U> Monad<Either<?, ?>, U> map(Function<? super B, ? extends U> mapper) {
		if (_isLeft)
			return left(_a);
		else
			return right(mapper.apply(_b));
	}

	@Override
	public <U> Monad<Either<?, ?>, U> flatMap(Function<? super B, ? extends Monad<Either<?, ?>, U>> mapper) {
		if(_isLeft)
			return left(_a);
		else
			return mapper.apply(_b);
	}

	@Override
	public <C, D> BiFunctor<Either<?, ?>, C, D> bimap(Function<? super A, ? extends C> f1,
			Function<? super B, ? extends D> f2) {
		if(_isLeft)
			return left(f1.apply(_a));
		else
			return right(f2.apply(_b));
	}

    @Override
    public <C> SplitFunctor<Either<?, ?>, A, C> repair(Function<? super A, ? extends C> left, Function<? super B, ? extends C> right) {
        C c = _isLeft?left.apply(_a):right.apply(_b);
        return right(c);
    }
}
