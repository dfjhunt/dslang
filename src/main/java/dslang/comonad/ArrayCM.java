
package dslang.comonad;

import static dslang.util.function.composition.Hole.__;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import dslang.util.function.composition.Partial;

public class ArrayCM<T> implements Comonad<ArrayCM<?>, T> {
    List<T> list;

    int index;

    public ArrayCM(List<T> l, int index) {
        this.list = l;
        this.index = index;
    }

    public List<T> getList() {
        return list;
    }

    public int getIndex() {
        return index;
    }

    public Optional<ArrayCM<T>> left() {
        return (index > 0) ? Optional.of(new ArrayCM<>(list, index - 1))
            : Optional.empty();
    }

    public Optional<ArrayCM<T>> right() {
        return (index < list.size() - 1) ? Optional.of(new ArrayCM<>(list, index + 1))
            : Optional.empty();
    }

    public T safeValue(int index, T defaultValue) {
        if ((index < 0) || (index >= list.size()))
            return defaultValue;
        else
            return list.get(index);
    }

    @Override
    public T extract() {
        return list.get(index);
    }

    @Override
    public <U> ArrayCM<U> map(Function<? super T, ? extends U> mapper) {
        return new ArrayCM<U>(list.stream().map(mapper).collect(Collectors.toList()), index);
    }
    
    @Override
    public <U, V extends Comonad<ArrayCM<?>, T>> Comonad<ArrayCM<?>, U> extend(Function<? super V, ? extends U> f) {
        List<U> uList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            // As long as ListCM is the only class that extends Comonad<ListCM<?>,T> this should be safe
            uList.add(f.apply((V) new ArrayCM<T>(list, i)));
        }
        return new ArrayCM<U>(uList, index);
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
        ArrayCM<Integer> l = new ArrayCM<>(Arrays.asList(1, 1, 2, 4, 2, 1, 1), 0);

        System.out.println(l.getList());

        BiFunction<ArrayCM<Integer>, Integer, Integer> sum = (x, y) -> {
            return x.getWindow(y, 0).stream().reduce(0, (a, b) -> a + b);
        };

        BiFunction<ArrayCM<Integer>, Integer, Double> avg = (x, i) -> {
            return ((double) sum.apply(x, i)) / i;
        };

        for (int i = 0; i < 10; i++) {
            ArrayCM<Double> l4 = (ArrayCM<Double>) l.extend(Partial.of(avg, __, i));
            System.out.println(l4.getList());
        }
    }

    

}
