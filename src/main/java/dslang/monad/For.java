package dslang.monad;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import dslang.monad.wrapper.ListM;
import dslang.util.Pair;
import dslang.util.function.Fluent;
import dslang.util.function.TriFunction;

public class For {
	@SuppressWarnings("unchecked")
	static public <M, A, B, C, N extends Monad<M, C>> N of(Monad<M, A> a, Monad<M, B> b, BiFunction<A, B, C> func) {
		return (N) a.flatMap(av -> b.map(bv -> func.apply(av, bv)));
	}

	@SuppressWarnings("unchecked")
	static public <M, A, B, C, X extends Monad<M, B>, Y extends Monad<M, C>> Y of(Monad<M, A> a, Function<A, X> bf,
			BiFunction<A, B, C> func) {
		return (Y) a.flatMap(av -> bf.apply(av).map(bv -> func.apply(av, bv)));
	}

	@SuppressWarnings("unchecked")
	static public <M, A, B, C, D, N extends Monad<M, D>> N of(Monad<M, A> a, Monad<M, B> b, Monad<M, C> c,
			TriFunction<A, B, C, D> func) {
		return (N) a.flatMap(av -> b.flatMap(bv -> c.map(cv -> func.apply(av, bv, cv))));
	}

	@SuppressWarnings("unchecked")
	static public <M, A, B, C, D, X extends Monad<M, B>, Y extends Monad<M, C>, Z extends Monad<M, D>> Z of(
			Monad<M, A> a, Function<A, X> bf, BiFunction<A, B, Y> cf, TriFunction<A, B, C, D> func) {
		return (Z) a.flatMap(av -> bf.apply(av).flatMap(bv -> cf.apply(av, bv).map(cv -> func.apply(av, bv, cv))));
	}

	static public void main(String args[]) {
		ListM<String> ranks = ListM
				.of(Arrays.asList("Ace", "King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2"));
		ListM<String> suits = ListM.of(Arrays.asList("Hearts", "Diamonds", "Clubs", "Spades"));

		// creates the cross product of (rank X suit) as pairs
		ListM<Pair<String, String>> deck = For.of(ranks, suits, Pair::of);

		deck.map(Fluent.of(System.out::println));
	}
}
