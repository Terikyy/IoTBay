package model.lineproducts;

public class OrderItem extends LineProduct {
    private int orderID;
    private double priceOnOrder;

    public OrderItem() {
        super();
    }

    public OrderItem(int productID, int orderID, int quantity, double priceOnOrder) {
        super(productID, quantity);
        this.orderID = orderID;
        this.priceOnOrder = priceOnOrder;
    }

    public int getOrderID() {
        return orderID;
    }

    public double getPriceOnOrder() {
        return priceOnOrder;
    }
}
