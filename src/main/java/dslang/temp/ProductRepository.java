package dslang.temp;

import dslang.monad.Monad;
import dslang.monad.wrapper.OptionM;

public interface ProductRepository<M> {
    public Monad<M, OptionM<Product>> findProduct(String id);
    public Monad<M, Boolean> saveProduct(Product product);
    public Monad<M, Void> incrementProductSales(String id, int quantity);
    
    public <A> Monad<M, A> create(A a);
}
