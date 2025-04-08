package model;

import java.util.Date;

public class Order {
    private final int orderID;
    private final int paymentID;
    private final Date orderDate;
    private int status; // 0: Pending, 1: Shipped, 2: Delivered, 3: Cancelled
    private int addressID; // Not final to allow for address changes

    public Order(int orderID, int paymentID, Date orderDate, int status, int addressID) {
        this.orderID = orderID;
        this.paymentID = paymentID;
        this.orderDate = orderDate;
        this.status = status;
        this.addressID = addressID;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

}
