package model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ShippingTest {
    ShippingManagement shipping;
    private final String shipmentId = "12345";
    private final String orderId = "54321";
    private final String shippingMethod = "Express";
    private final LocalDate shipmentDate = LocalDate.now();
    private final String address = "B11 UTS";
    private final boolean isFinalised = false;

    public ShippingTest() {
        shipping = new ShippingManagement(Integer.parseInt(shipmentId), Integer.parseInt(orderId), shipmentDate, address, shippingMethod, isFinalised);
    }

    @Test
    public void testGetShipmentId() {
        assertEquals(Integer.parseInt(shipmentId), shipping.getShipmentId());
    }

    @Test
    public void testGetOrderId() {
        assertEquals(Integer.parseInt(orderId), shipping.getOrderId());
    }

    @Test
    public void testGetShippingMethod() {
        assertEquals(shippingMethod, shipping.getShippingMethod());
    }

    @Test
    public void testGetShipmentDate() {
        assertEquals(shipmentDate, shipping.getShipmentDate());
    }

    @Test
    public void testGetAddress() {
        assertEquals(address, shipping.getAddress());
    }

    @Test
    public void testIsFinalised() {
        assertEquals(isFinalised, shipping.isFinalised());
    }

    @Test
    public void testSetShipmentId() {
        String newShipmentId = "67890";
        shipping.setShipmentId(Integer.parseInt(newShipmentId));
        assertEquals(Integer.parseInt(newShipmentId), shipping.getShipmentId());
    }

    @Test
    public void testSetOrderId() {
        String newOrderId = "09876";
        shipping.setOrderId(Integer.parseInt(newOrderId));
        assertEquals(Integer.parseInt(newOrderId), shipping.getOrderId());
    }

    @Test
    public void testSetShippingMethod() {
        String newShippingMethod = "Standard";
        shipping.setShippingMethod(newShippingMethod);
        assertEquals(newShippingMethod, shipping.getShippingMethod());
    }

    @Test
    public void testSetShipmentDate() {
        LocalDate newShipmentDate = LocalDate.now();
        shipping.setShipmentDate(newShipmentDate);
        assertEquals(shipmentDate, shipping.getShipmentDate());
    }

    @Test
    public void testSetAddress() {
        String newAddress = "B12 UTS";
        shipping.setAddress(newAddress);
        assertEquals(newAddress, shipping.getAddress());
    }

    @Test
    public void testSetFinalised() {
        boolean newFinalised = true;
        shipping.setFinalised(newFinalised);
        assertEquals(newFinalised, shipping.isFinalised());
    }

}
