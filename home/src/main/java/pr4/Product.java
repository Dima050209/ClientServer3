package pr4;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private double price;
    private String factoryName;

    public Product(int id, String name, double price, String factoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.factoryName = factoryName;
    }
    public Product(String name, double price, String factoryName) {
        this.name = name;
        this.price = price;
        this.factoryName = factoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && Objects.equals(name, product.name) && Objects.equals(factoryName, product.factoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, factoryName);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", factoryName='" + factoryName + '\'' +
                '}';
    }
}
