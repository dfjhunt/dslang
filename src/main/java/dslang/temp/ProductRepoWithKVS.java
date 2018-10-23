package dslang.temp;

import dslang.monad.Monad;
import dslang.monad.wrapper.OptionM;

public class ProductRepoWithKVS<M> implements ProductRepository<M> {

    KeyValueStore<M> store;
    
    public ProductRepoWithKVS(KeyValueStore<M> kvs){
        store = kvs;
    }
    
    @Override
    public Monad<M, OptionM<Product>> findProduct(String id) {
        return store.get(id);
    }

    @Override
    public Monad<M, Boolean> saveProduct(Product product) {
        return store.put(product.getName(), product);
    }

    @Override
    public Monad<M, Void> incrementProductSales(String id, int quantity) {
        return null;
    }

    @Override
    public <A> Monad<M, A> create(A a) {
        return store.create(a);
    }

}
