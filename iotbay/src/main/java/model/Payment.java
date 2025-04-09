package model;

import java.util.Date;

public class Payment {
    private final int paymentID;
    private final int orderID;
    private final double amountPaid;
    private final String paymentMethod;
    private final Date paymentDate;
    private int paymentStatus; // 0: Pending, 1: Completed, 2: Failed

    public Payment(int paymentID, int orderID, String paymentMethod, double amountPaid, Date paymentDate, int paymentStatus) {
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

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
