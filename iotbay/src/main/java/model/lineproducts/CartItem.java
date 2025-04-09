package model.lineproducts;

public class CartItem extends LineProduct {
    private final int orderID;

    public CartItem(int productID, int orderID, int quantity) {
        super(productID, quantity);
        this.orderID = orderID;
    }

    public int getOrderID() {
        return orderID;
    }
}
