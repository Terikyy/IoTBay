package model.lineproducts;

public class CartItem extends LineProduct {
    private final Integer userID; // Use Integer since it can be null (when not logged in)

    public CartItem(int productID, Integer userID, int quantity) {
        super(productID, quantity);
        this.userID = userID;
    }

    public Integer getUserId() {
        return userID;
    }
}
