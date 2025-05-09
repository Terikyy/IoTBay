package model;

import java.util.Date;

public class Product extends IDObject {
    private String name;
    private String description;
    private double price;
    private int stock;
    private Date releaseDate;

    public Product(String name, String description, double price, int stock, Date releaseDate) {
        super();
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.releaseDate = releaseDate;
    }

    public Product(int productId, String name, String description, double price, int stock, Date releaseDate) {
        this(name, description, price, stock, releaseDate);
        setId(productId);
    }

    public int getProductID() {
        return getId();
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
