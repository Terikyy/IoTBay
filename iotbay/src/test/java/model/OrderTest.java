package model;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    private final Order order;
    private final Integer orderID = Integer.MAX_VALUE;
    private final Integer userID = Integer.MAX_VALUE;
    private final String orderStatus = Order.ORDER_STATUS_PENDING;
    private final Date orderDate = new Date(System.currentTimeMillis());
    private final double totalPrice = 100.0;

    public OrderTest() {
        order = new Order(orderID, userID, orderStatus, orderDate, totalPrice);
    }

    @Test
    public void testGetOrderID() {
        assertEquals(orderID, order.getOrderID());
    }

    @Test
    public void testGetUserID() {
        assertEquals(userID, order.getUserID());
    }

    @Test
    public void testGetOrderStatus() {
        assertEquals(orderStatus, order.getOrderStatus());
    }

    @Test
    public void testGetOrderDate() {
        assertEquals(orderDate, order.getOrderDate());
    }

    @Test
    public void testGetTotalPrice() {
        assertEquals(totalPrice, order.getTotalPrice(), 0.0001);
    }

    @Test
    public void testSetOrderStatus() {
        String newStatus = Order.ORDER_STATUS_PAID;
        order.setOrderStatus(newStatus);
        assertEquals(newStatus, order.getOrderStatus());
    }

}
