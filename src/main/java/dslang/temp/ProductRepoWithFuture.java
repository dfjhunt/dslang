package dslang.temp;

import dslang.monad.Monad;
import dslang.monad.wrapper.FutureM;
import dslang.monad.wrapper.FutureM.t;
import dslang.monad.wrapper.OptionM;

public class ProductRepoWithFuture implements ProductRepository<FutureM.t> {

    @Override
    public Monad<FutureM.t, OptionM<Product>> findProduct(String id) {
        return FutureM.sunit(OptionM.of(new Product("Joe")));
    }

    @Override
    public Monad<FutureM.t, Boolean> saveProduct(Product product) {
        return FutureM.sunit(true);
    }

    @Override
    public Monad<FutureM.t, Void> incrementProductSales(String id, int quantity) {
        return FutureM.sunit(null);
    }

    @Override
    public <A> Monad<t, A> create(A a) {
        return FutureM.sunit(a);
    }
    
}
