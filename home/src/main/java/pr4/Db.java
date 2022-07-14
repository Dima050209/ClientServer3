package pr4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Db {
    private final Connection connection;

    public Db(String dbName) {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);

            try(Statement statement = connection.createStatement()){
                String createTable = "CREATE TABLE IF NOT EXISTS PRODUCTS(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "NAME TEXT NOT NULL," +
                        "PRICE REAL NOT NULL," +
                        "FACTORY_NAME TEXT NOT NULL)";
                statement.execute(createTable);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void createProduct(Product product){
        String sql = "INSERT INTO PRODUCTS (NAME, PRICE,FACTORY_NAME) VALUES (?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getFactoryName());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Product> getProducts(){
        List<Product> resultList = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTS";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                resultList.add(new Product(resultSet.getInt("ID"),
                        resultSet.getString("NAME"),
                        resultSet.getDouble("PRICE"),
                        resultSet.getString("FACTORY_NAME")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
    public void update(int id, Product product) {
        boolean comaFlag = true;
        String sql = "UPDATE PRODUCTS SET";

        if(product.getName() != null) {
            comaFlag = false;
            sql += " NAME = '" + product.getName() + "'";
        }
        if(product.getName() != null) {
            if(comaFlag) {
                sql += " PRICE = " + product.getPrice();
                comaFlag = false;
            } else {
                sql += ", PRICE = " + product.getPrice();
            }
        }
        if(product.getName() != null) {
            if(comaFlag) {
                comaFlag = false;
                sql += " FACTORY_NAME = '" + product.getFactoryName() + "'";
            } else {
                sql += ", FACTORY_NAME = '" + product.getFactoryName() + "'";
            }
        }
        sql += " WHERE ID = " + id;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Product delete(int id) {
        Product product = this.getProductById(id);
        String sql = "DELETE FROM PRODUCTS WHERE ID = " + id;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
    public List<Product> getProductsByFilter(ProductFilter productFilter){
        List<Product> resultList = new ArrayList<>();
        List<String> whereFilters = Stream.of(
            SqlBuilder.gte(productFilter.getPriceFrom(),"PRICE"),
                SqlBuilder.lte(productFilter.getPriceTo(),"PRICE"),
                SqlBuilder.startWith(productFilter.getNameStartWith(),"NAME"),
                SqlBuilder.endWith(productFilter.getNameEndWith(),"NAME")
        ).filter(Objects::nonNull).collect(Collectors.toList());
        String sql = "SELECT * FROM PRODUCTS";
        if(!whereFilters.isEmpty()){
            sql += " WHERE " + String.join(" AND ", whereFilters);
        }
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                resultList.add(new Product(resultSet.getInt("ID"),
                        resultSet.getString("NAME"),
                        resultSet.getDouble("PRICE"),
                        resultSet.getString("FACTORY_NAME")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
    public Product getProductById(int id){
        Product product = null;
        String sql = "SELECT * FROM PRODUCTS WHERE ID == ?" ;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                product = new Product(resultSet.getInt("ID"),
                        resultSet.getString("NAME"),
                        resultSet.getDouble("PRICE"),
                        resultSet.getString("FACTORY_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public static void main(String[] args) {
        Db db = new Db("products.db");
        Product product = new Product("prod1", 123.12, "fact1");
        db.createProduct(product);
        List<Product> allProducts = db.getProducts();
        System.out.println(allProducts);

        Product product1 = new Product("apple", 123.122, "fact2");
        db.createProduct(product1);

        ProductFilter productFilter = new ProductFilter();
        productFilter.setNameStartWith("app");
        List<Product> filteredProducts = db.getProductsByFilter(productFilter);
        System.out.println(filteredProducts);
        db.update(2,product1);
        db.delete(1);
    }
}
