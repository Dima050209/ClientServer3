package hw3;

import java.util.ArrayList;

public class Product {
    private double price;
    private String id;
    private String productGroup;
    private int amount;
    private static ArrayList<Product> productsList = new ArrayList<>();
    public static ArrayList<String> productGroupsList = new ArrayList<>();

    public Product(String id, int amount, double price) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        productsList.add(this);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }
    public void takeAmount(int amount) {
        this.amount -= amount;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        if(this.productGroup == null && productGroupsList.contains(productGroup)) {
            System.out.println("Group : " + productGroup);
            this.productGroup = productGroup;
        }
    }
    public static Product getProductById(String id){
        for(int i = 0; i < productsList.size(); ++i){
            if(productsList.get(i).getId().equals(id))
                return productsList.get(i);
        }
        return null;
    }
    public static void addProductGroup(String name) {
        if(!productGroupsList.contains(name))
            productGroupsList.add(name);
    }

    @Override
    public String toString() {
        return "hw2.Product{" +
                "price=" + price +
                ", id='" + id + '\'' +
                ", productGroup='" + productGroup + '\'' +
                ", amount=" + amount +
                '}';
    }
}
