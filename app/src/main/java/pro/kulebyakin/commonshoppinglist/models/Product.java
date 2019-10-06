package pro.kulebyakin.commonshoppinglist.models;

import android.graphics.drawable.Drawable;

public class Product {

    public int image;
    public int position;
    public String name;
    public double price;
    public double quantity;

    // flag when item swiped
    public boolean swiped = false;
    public boolean checked = false;

    public Product() {
    }

    public Product(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public Product(int position, int image, String name) {
        this.image = image;
        this.name = name;
        this.position = position;
    }

    public Product(int position, int image, String name, double quantity) {
        this.image = image;
        this.name = name;
        this.position = position;
        this.quantity = quantity;
    }
    public Product(int position, int image, String name, double quantity, double price) {
        this.image = image;
        this.name = name;
        this.quantity = quantity;
        this.position = position;
        this.price = price;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public boolean isSwiped() {
        return swiped;
    }
    public boolean isChecked() {
        return checked;
    }
}
