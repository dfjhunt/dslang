package dslang.temp.clean;

public class ProductRepoWithOpt implements ProductRepository {

    @Override
    public M<Product> findProduct(String id) {
        return OptionalM.of(new Product("SPAM"));
    }

    @Override
    public M<Boolean> saveProduct(Product product) {
        return OptionalM.of(true);
    }

    @Override
    public M<Void> incrementProductSales(String id, int quantity) {
        return OptionalM.of(null);
    }

    @Override
    public <A> M<A> create(A a) {
        return OptionalM.of(a);
    }

}
