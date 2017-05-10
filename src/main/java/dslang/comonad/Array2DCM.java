
package dslang.comonad;

import java.util.function.Function;

import dslang.monad.wrapper.OptionM;
import dslang.util.Pair;

public class Array2DCM<T> implements Comonad<Array2DCM<?>, T> {

    Object[][] _grid;

    Pair<Integer, Integer> _index;

    public Array2DCM(Object[][] grid, Pair<Integer, Integer> index) {
        _grid = grid;
        _index = index;
    }

    public OptionM<Array2DCM<T>> up() {
        if (_index.right() > 0) {
            return OptionM.of(new Array2DCM<>(_grid, _index.second(y -> y - 1)));
        } else {
            return OptionM.empty();
        }
    }

    public OptionM<Array2DCM<T>> down() {
        if (_index.right() < _grid.length - 1) {
            return OptionM.of(new Array2DCM<>(_grid, _index.second(y -> y + 1)));
        } else {
            return OptionM.empty();
        }
    }

    public OptionM<Array2DCM<T>> left() {
        if (_index.left() > 0) {
            return OptionM.of(new Array2DCM<>(_grid, _index.first(x -> x - 1)));
        } else {
            return OptionM.empty();
        }
    }

    public OptionM<Array2DCM<T>> right() {
        if (_index.left() < _grid[_index.right()].length - 1) {
            return OptionM.of(new Array2DCM<>(_grid, _index.first(x -> x + 1)));
        } else {
            return OptionM.empty();
        }
    }

    @Override
    public <B> Array2DCM<B> map(Function<? super T, ? extends B> mapper) {
        Object[][] temp = cloneGrid();
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                temp[i][j] = mapper.apply((T) _grid[i][j]);
            }
        }
        return new Array2DCM<>(temp, _index);
    }

    @Override
    public T extract() {
        return (T) _grid[_index.left()][_index.right()];
    }

    @Override
    public <U, V extends Comonad<Array2DCM<?>, T>> Array2DCM<U> extend(Function<? super V, ? extends U> f) {

        Object[][] temp = cloneGrid();
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                temp[i][j] = f.apply((V) new Array2DCM(_grid, Pair.of(i, j)));
            }
        }
        
        return new Array2DCM<>(temp, _index);
    }

    @Override
    public Array2DCM<Comonad<Array2DCM<?>, T>> duplicate() {
        return null;
    }

    private Object[][] cloneGrid() {
        Object[][] ts = _grid.clone();
        for (int i = 0; i < ts.length; i++) {
            ts[i] = _grid[i].clone();
        }
        return ts;
    }

    private static String toString(Object[][] arr) {

        StringBuilder temp = new StringBuilder("{\n");
        for (int i = 0; i < arr.length; i++) {
            temp.append("{");
            for (int j = 0; j < arr[i].length; j++) {
                temp.append(arr[i][j] + ", ");
            }
            temp.append("}\n");
        }
        temp.append("}");

        return temp.toString();
    }

    public String toString() {
        return toString(_grid);
    }

    public static int open(OptionM<Array2DCM<Integer>> val) {
        return val.map(g -> g.extract()).repair(v -> 0, x -> x).get();
    }

    public static int lifeStep(Comonad<Array2DCM<?>, Integer> cg) {
        Array2DCM<Integer> g = (Array2DCM<Integer>) cg;
        OptionM<Array2DCM<Integer>> up = g.up();
        OptionM<Array2DCM<Integer>> down = g.down();
        int sum = open(up.flatMap(gr -> gr.left())) + //
                        open(up) + //
                        open(up.flatMap(gr -> gr.right())) + //
                        open(g.left()) + //
                        open(g.right()) + //
                        open(down.flatMap(gr -> gr.left())) + //
                        open(down) + //
                        open(down.flatMap(gr -> gr.right()));
        int c = g.extract();
        if((c==1)&&((sum==2)||(sum==3)))
            return 1;
        else if((c==0)&&(sum==3))
            return 1;
        else
            return 0;
    }

    public static int lifeSteptest(Comonad<Array2DCM<?>, Integer> cg) {
        Array2DCM<Integer> g = (Array2DCM<Integer>) cg;
        OptionM<Array2DCM<Integer>> up = g.up();
        OptionM<Array2DCM<Integer>> down = g.down();
        int sum = open(up.flatMap(gr -> gr.left())) + //
                        open(up) + //
                        open(up.flatMap(gr -> gr.right())) + //
                        open(g.left()) + //
                        g.extract() + //
                        open(g.right()) + //
                        open(down.flatMap(gr -> gr.left())) + //
                        open(down) + //
                        open(down.flatMap(gr -> gr.right()));
        return sum;
    }
    
    public static void main(String args[]) {
        Integer temp[][] = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 } };
        Array2DCM<Integer> g = new Array2DCM<>(temp, Pair.of(0, 0));

        g = g.extend(x -> x.extract() + 2);

        System.out.println(g);

        Integer lifeArr[][] = { { 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 0 }, { 0, 1, 0, 1, 0 }, { 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0 } };
        Array2DCM<Integer> life = new Array2DCM<>(lifeArr, Pair.of(0, 0));

        life = life.extend(Array2DCM::lifeStep);

        System.out.println(life);

        life = life.extend(Array2DCM::lifeStep);

        System.out.println(life);

        life = life.extend(Array2DCM::lifeStep);

        System.out.println(life);
    }
}
