package model;
import java.time.LocalDate;



public class ShippingManagement extends IDObject {
    private int shipmentID;
    private int orderID;
    private LocalDate shipmentDate;
    private String address;
    private String deliveryMethod;

    public ShippingManagement(int shipmentID, int orderID, LocalDate shipmentDate, String address, String deliveryMethod) {
        this.shipmentID = shipmentID;
        this.orderID = orderID;
        this.shipmentDate = shipmentDate;
        this.address = address;
        this.deliveryMethod = deliveryMethod;
    }
    
    public int getShipmentId() {
        return shipmentID;
    }  
    public int getOrderId() {
        return orderID;
    }
    public LocalDate getShipmentDate() {
        return shipmentDate;
    }

    public String getAddress() {
        return address;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    
    public void setAddress(String address){
        this.address = address;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
}
