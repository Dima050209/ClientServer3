import org.junit.jupiter.api.Test;
import pr4.Db;
import pr4.Product;
import pr4.ProductFilter;

import java.util.ArrayList;
import java.util.List;

public class DBTest {
    @Test
    void dbTest() throws Exception {
        int min = 50;
        int max = 100000;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        System.out.println(random_int);
        Db db = new Db("target/"+random_int);
        Product p1 = new Product("apple", 100, "factory1");
        Product p2 = new Product("banana", 70, "factory2");
        Product p3 = new Product("orange", 50, "factory2");

        db.createProduct(p1);
        db.createProduct(p2);
        db.createProduct(p3);

        List<Product> myProducts = new ArrayList<>();
        myProducts.add(p1);
        myProducts.add(p2);
        myProducts.add(p3);
        List<Product> allProducts = db.getProducts();
        for(int i = 0; i < 3; ++i){
            System.out.println(allProducts.get(i) + " - " + myProducts.get(i));
            if(!allProducts.get(i).equals(myProducts.get(i))){
                throw new Exception();
            }
        }
        Product p4 = new Product("updated", 101, "factory4");
        ProductFilter productFilter = new ProductFilter();
        productFilter.setNameStartWith("upd");
        productFilter.setNameEndWith("ed");
        db.update(1, p4);
        allProducts = db.getProductsByFilter(productFilter);
        System.out.println(allProducts.get(0));
        if(!allProducts.get(0).equals(p4)){
            throw new Exception();
        }

        db.delete(1);
        allProducts = db.getProductsByFilter(productFilter);
        System.out.println(allProducts);
        if(!allProducts.isEmpty() && allProducts.get(0).equals(p4)){
            throw new Exception();
        }
    }
}
