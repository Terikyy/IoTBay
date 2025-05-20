package model;
import java.time.LocalDate;


public class ShippingManagement extends IDObject {
    private int shipmentId;
    private int orderId;
    private String shippingMethod;
    private LocalDate shipmentDate;
    private String address;
    private boolean isFinalised;

    public ShippingManagement(int shipmentId, int orderId, LocalDate shipmentDate, String address, String shippingMethod, boolean isFinalised) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.shipmentDate = shipmentDate;
        this.address = address;
        this.shippingMethod = shippingMethod;
        this.isFinalised = false; // Default to not shipped
    }

    public int getShipmentId() {
        return shipmentId;
    }
    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getShippingMethod() {
        return shippingMethod;
    }
    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
    public LocalDate getShipmentDate() {
        return shipmentDate;
    }
    public void setShipmentDate(LocalDate shipmentDate) {
        this.shipmentDate = shipmentDate;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public boolean isFinalised() {
        return isFinalised;
    }
    public void setFinalised(boolean Finalised) {
        isFinalised = Finalised;
    }

}
