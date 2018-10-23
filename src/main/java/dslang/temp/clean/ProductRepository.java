package dslang.temp.clean;

public interface ProductRepository {
    public M<Product> findProduct(String id);
    public M<Boolean> saveProduct(Product product);
    public M<Void> incrementProductSales(String id, int quantity);
    
    public <A> M<A> create(A a);
}

