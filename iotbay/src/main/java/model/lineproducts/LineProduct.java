package model.lineproducts;

public abstract class LineProduct {
    private int productID;
    private int quantity;

    public LineProduct() {
        super();
    }

    public LineProduct(int productID, int quantity) {
        this.productID = productID;
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
