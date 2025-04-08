package model;

import java.util.Date;

public class Product {
    int productID;
    String name;
    String description;
    double price;
    int stock;
    Date releaseDate;

    public Product(int productID, String name, String description, double price, int stock, Date releaseDate) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.releaseDate = releaseDate;
    }

    public int getProductID() {
        return productID;
    }

    // This method is not supposed to be used
    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
