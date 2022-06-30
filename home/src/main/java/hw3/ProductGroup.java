package hw3;

import java.util.ArrayList;

public class ProductGroup {
    private String groupId;
    private ArrayList<Product> allocator;
    private static ArrayList<ProductGroup> productGroupsList;
    public ProductGroup(String id) {
        this.groupId = id;
    }
    public void addProduct(Product product){
        if(!allocator.contains(product))
            allocator.add(product);
    }
    public Product getProductById(String id){
        for(int i = 0; i < allocator.size(); ++i){
            if(allocator.get(i).getId() == id)
                return allocator.get(i);
        }
        return null;
    }
}
