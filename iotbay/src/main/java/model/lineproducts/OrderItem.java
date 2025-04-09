package model.lineproducts;

public class OrderItem extends LineProduct {
    private final int orderID;
    private final double priceOnOrder;

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
