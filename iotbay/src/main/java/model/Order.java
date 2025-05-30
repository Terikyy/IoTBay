package model;

import java.sql.Date;

public class Order extends IDObject {
    private Integer userID;
    private String orderStatus;
    private Date orderDate;
    private double totalPrice;

    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_PAID = "PAID";
    public static final String ORDER_STATUS_SHIPPING = "SHIPPING";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    public Order() { // default constructor
        super();
    }

    public Order(Integer userID, String orderStatus, Date orderDate, double totalPrice) {
        super();
        this.userID = userID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public Order(int orderId, Integer userID, String orderStatus, Date orderDate, double totalPrice) {
        this(userID, orderStatus, orderDate, totalPrice);
        setId(orderId);
    }

    public int getOrderID() {
        return getId();
    }

    public Integer getUserID() {
        return userID;
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
