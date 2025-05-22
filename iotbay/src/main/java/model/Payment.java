package model;

import java.util.Date;

public class Payment extends IDObject {
    private int orderID;
    private double amountPaid;
    private String paymentMethod;
    private Date paymentDate;
    private String paymentStatus;

    public Payment() {
        super();
    }

    public Payment(int orderID, String paymentMethod, double amountPaid, Date paymentDate, String paymentStatus) {
        super();
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    public Payment(int paymentId, int orderID, String paymentMethod, double amountPaid, Date paymentDate, String paymentStatus) {
        this(orderID, paymentMethod, amountPaid, paymentDate, paymentStatus);
        setId(paymentId);
    }

    public int getPaymentID() {
        return getId();
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
