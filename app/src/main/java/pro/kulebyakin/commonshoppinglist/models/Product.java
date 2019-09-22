package pro.kulebyakin.commonshoppinglist.models;

public class Product {

    public String productName;
    public int unitPrice;
    public int unitQuantity;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(String productName, int unitPrice, int unitQuantity) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.unitQuantity = unitQuantity;
    }

}
