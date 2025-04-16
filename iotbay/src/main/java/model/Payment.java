package model;

import java.util.Date;

public class Payment {
    private final int paymentID;
    private final int orderID;
    private final double amountPaid;
    private final String paymentMethod;
    private final Date paymentDate;
    private String paymentStatus;

    public Payment(int paymentID, int orderID, String paymentMethod, double amountPaid, Date paymentDate, String paymentStatus) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentID() {
        return paymentID;
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
