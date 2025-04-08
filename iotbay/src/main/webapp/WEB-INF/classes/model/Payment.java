package model;

public class Payment {
    private final int paymentID;
    private final double amount;
    private final String paymentMethod;
    private final String paymentDate;

    public Payment(int paymentID, double amount, String paymentMethod, String paymentDate) {
        this.paymentID = paymentID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
}
