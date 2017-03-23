package dslang.monad;

import java.util.Arrays;
import java.util.function.BiFunction;

import org.junit.Assert;
import org.junit.Test;

import dslang.monad.wrapper.ListM;
import dslang.util.Pair;
import dslang.util.function.Fluent;

public class StateTest {
	@Test
	public void testFlatMap() {
		State<Integer, ListM<Integer>> t = State.sunit(new ListM<>());
		BiFunction<ListM<Integer>, Integer, ListM<Integer>> add = Fluent.ofBC(ListM::add);
		t = t.flatMap(l -> new State<>(s -> Pair.of(add.apply(l, s), s + 1)));
		t = t.flatMap(l -> new State<>(s -> Pair.of(add.apply(l, s), s + 1)));
		t = t.flatMap(l -> new State<>(s -> Pair.of(add.apply(l, s), s + 1)));
		t.run(0).mapPair((a, s) -> {
			Assert.assertEquals(Arrays.asList(0, 1, 2), a.unwrap());
			Assert.assertEquals((Integer) 3, s);
			return null;
		});
	}
	
	@Test
	public void testModify() {
		State<Integer, Void> s = State.modify(x->x+1);
		Assert.assertEquals(Pair.of(null, 3), s.run(2));
		
	}
	
}
