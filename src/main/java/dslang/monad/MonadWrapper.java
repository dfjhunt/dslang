package dslang.monad;

public interface MonadWrapper<M,T,WM> extends Monad<M,T> {
    public WM unwrap();
}
