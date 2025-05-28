package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class PaymentTest {
    private Payment payment;
    private final int paymentId = 123;
    private final int orderId = 456;
    private final double amountPaid = 99.99;
    private final String paymentMethod = "CreditCard";
    private final Date paymentDate = new Date();
    private final String paymentStatus = Payment.PAYMENT_STATUS_PENDING;

    public PaymentTest() {
        // use constructor that sets the ID
        payment = new Payment(paymentId, orderId, paymentMethod, amountPaid, paymentDate, paymentStatus);
    }

    @Test
    public void testGetPaymentID() {
        assertEquals(paymentId, payment.getPaymentID());
    }

    @Test
    public void testGetOrderID() {
        assertEquals(orderId, payment.getOrderID());
    }

    @Test
    public void testGetAmountPaid() {
        assertEquals(amountPaid, payment.getAmountPaid(), 0.0001);
    }

    @Test
    public void testGetPaymentMethod() {
        assertEquals(paymentMethod, payment.getPaymentMethod());
    }

    @Test
    public void testGetPaymentDate() {
        assertEquals(paymentDate, payment.getPaymentDate());
    }

    @Test
    public void testGetPaymentStatus() {
        assertEquals(paymentStatus, payment.getPaymentStatus());
    }

    @Test
    public void testSetPaymentStatus() {
        String newStatus = Payment.PAYMENT_STATUS_COMPLETED;
        payment.setPaymentStatus(newStatus);
        assertEquals(newStatus, payment.getPaymentStatus());
    }

    @Test
    public void testSetPaymentDate() {
        Date newDate = new Date(paymentDate.getTime() + 86400000L); // +1 day
        payment.setPaymentDate(newDate);
        assertEquals(newDate, payment.getPaymentDate());
    }
}