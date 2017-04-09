package dslang.util.function.checked;

@FunctionalInterface
public interface CheckedSupplier< R, E extends Exception> {
   R get() throws E;
}
