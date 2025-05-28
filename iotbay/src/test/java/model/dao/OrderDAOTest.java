package model.dao;

import model.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDAOTest {
    OrderDAO orderDAO;
    Order order;
    private final Integer orderID = Integer.MAX_VALUE;
    private final Integer userID = Integer.MAX_VALUE;
    private final String orderStatus = Order.ORDER_STATUS_PENDING;
    private final Date orderDate = new Date(System.currentTimeMillis());
    private final double totalPrice = 100.0;

    public OrderDAOTest() throws Exception {
        DBConnector db = new DBConnector();
        try {
            orderDAO = new OrderDAO(db.openConnection());
            order = new Order(orderID, userID, orderStatus, orderDate, totalPrice);
        } catch (Exception e) {
            throw new Exception("Failed to initialize OrderDAO: " + e.getMessage(), e);
        }
    }

    @Test
    public void testGetOrderById() {
        try {
            Order retrievedOrder = orderDAO.findById(1);
            assertNotNull(retrievedOrder);
        } catch (Exception e) {
            fail("Get order by ID failed: " + e.getMessage());
        }
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void testInsert() {
        try {
            orderDAO.insert(order);
        } catch (Exception e) {
            throw new RuntimeException("Insert failed: " + e.getMessage(), e);
        }

        try {
            Order retrievedOrder = orderDAO.findById(orderID);
            assertNotNull(retrievedOrder);
            assertEquals(orderID, retrievedOrder.getOrderID());
            assertEquals(userID, retrievedOrder.getUserID());
            assertEquals(orderStatus, retrievedOrder.getOrderStatus());
            assertEquals(orderDate, retrievedOrder.getOrderDate());
            assertEquals(totalPrice, retrievedOrder.getTotalPrice(), 0.0001);
        } catch (Exception e) {
            fail("Failed to retrieve order after insert: " + e.getMessage());
        }
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void testUpdate() {
        try {
            order.setOrderStatus(Order.ORDER_STATUS_PAID);
            orderDAO.update(order);
        } catch (Exception e) {
            fail("Update failed: " + e.getMessage());
        }

        try {
            Order updatedOrder = orderDAO.findById(orderID);
            assertNotNull(updatedOrder);
            assertEquals(Order.ORDER_STATUS_PAID, updatedOrder.getOrderStatus());
        } catch (Exception e) {
            fail("Failed to retrieve order after update: " + e.getMessage());
        }
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void testDelete() {
        try {
            orderDAO.deleteById(orderID);
        } catch (Exception e) {
            fail("Delete failed: " + e.getMessage());
        }

        try {
            Order deletedOrder = orderDAO.findById(orderID);
            assertNull(deletedOrder);
        } catch (Exception e) {
            fail("Failed to retrieve order after delete: " + e.getMessage());
        }
    }
}
