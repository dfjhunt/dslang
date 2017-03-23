
package dslang.comonad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import dslang.util.function.composition.Hole;
import dslang.util.function.composition.Partial;

public class ListCM<T> implements Comonad<ListCM<?>, T> {
    List<T> list;

    int index;

    public ListCM(List<T> l, int index) {
        this.list = l;
        this.index = index;
    }

    public List<T> getList() {
        return list;
    }

    public int getIndex() {
        return index;
    }

    public Optional<ListCM<T>> left() {
        return (index > 0) ? Optional.of(new ListCM<>(list, index - 1))
            : Optional.empty();
    }

    public Optional<ListCM<T>> right() {
        return (index < list.size() - 1) ? Optional.of(new ListCM<>(list, index + 1))
            : Optional.empty();
    }

    public T safeValue(int index, T defaultValue) {
        if ((index < 0) || (index >= list.size()))
            return defaultValue;
        else
            return list.get(index);
    }

    public T safeRelValue(int relIndex, T defaultValue) {
        return safeValue(index + relIndex, defaultValue);
    }

    @Override
    public T extract() {
        return list.get(index);
    }

    @Override
    public <U> Comonad<ListCM<?>, U> extend(Function<Comonad<ListCM<?>, T>, U> f) {
        return extendCM(x -> f.apply((ListCM<T>) x));
    }

    public <U> Comonad<ListCM<?>, U> extendCM(Function<ListCM<T>, U> f) {
        List<U> uList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            uList.add(f.apply(new ListCM<T>(list, i)));
        }
        return new ListCM<U>(uList, index);
    }

    @Override
    public Comonad<ListCM<?>, Comonad<ListCM<?>, T>> duplicate() {
        List<Comonad<ListCM<?>, T>> l = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            l.add((Comonad<ListCM<?>, T>) (new ListCM<T>(list, i)));
        }
        return new ListCM<>(l, index);
    }

    public List<T> getWindow(int windowSize, T pad) {
        List<T> rl = new ArrayList<>();

        int halfWindow = windowSize / 2;

        for (int i = index - halfWindow; i <= index + halfWindow; i++) {
            rl.add(safeValue(i, pad));
        }
        return rl;
    }

    public static void main(String args[]) {
        ListCM<Integer> l = new ListCM<>(Arrays.asList(1, 1, 2, 4, 2, 1, 1), 0);

        System.out.println(l.getList());

        BiFunction<ListCM<Integer>, Integer, Integer> sum = (x, y) -> {
            return x.getWindow(y, 0).stream().reduce(0, (a, b) -> a + b);

        };

        BiFunction<ListCM<Integer>, Integer, Double> avg = (x, i) -> {
            return ((double) sum.apply(x, i)) / i;

        };

        // ListCM<Integer> l2 = (ListCM<Integer>) l.extendCM(sum);
        // System.out.println(l2.getList());
        //
        // ListCM<Integer> l3 = (ListCM<Integer>) l2.extendCM(sum);
        // System.out.println(l3.getList());

        for (int i = 0; i < 10; i++) {
            ListCM<Double> l4 = (ListCM<Double>) l.extendCM(Partial.of(avg, Hole.__, i));
            System.out.println(l4.getList());
        }
    }

}
