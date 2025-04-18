package model;

import java.util.Date;

public class Order {
    private final int orderID;
    private final Integer userID;
    private int addressID; // Not final to allow for address change
    private String trackingNumber;
    private String orderStatus; 
    private final Date orderDate;
    private final double totalPrice;


    public Order(int orderID, Integer userID, int addressID, String trackingNumber, String orderStatus, Date orderDate, double totalPrice) {
        this.orderID = orderID;
        this.userID = userID;
        this.addressID = addressID;
        this.trackingNumber = trackingNumber;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public int getOrderID() {
        return orderID;
    }

    public Integer getUserID() {
        return userID;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
