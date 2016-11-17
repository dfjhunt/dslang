package dslang.util.function;

@FunctionalInterface
public interface CheckedSupplier< R, E extends Exception> {
   R get() throws E;
}
