package model;

import java.util.Date;

public class Order {
    private final int orderID;
    private final Integer userID;
    private final Date orderDate;
    private int orderStatus; // 0: Pending, 1: Shipped, 2: Delivered, 3: Cancelled
    private int addressID; // Not final to allow for address changes
    private Integer trackingNumber;
    private double totalPrice;


    public Order(int orderID, Integer userID, int addressID, Integer trackingNumber, int orderStatus, Date orderDate, double totalPrice) {
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

    public Date getOrderDate() {
        return orderDate;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public Integer getUserID() {
        return userID;
    }

    public Integer getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(Integer trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
