package pro.kulebyakin.commonshoppinglist.models;

import android.graphics.drawable.Drawable;

public class Product {

    public int image;
    public Drawable imageDrw;
    public String name;
    public boolean expanded = false;
    public boolean parent = false;

    // flag when item swiped
    public boolean swiped = false;

    public Product() {
    }

    public Product(int image, String name) {
        this.image = image;
        this.name = name;
    }
    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public Drawable getImageDrw() {
        return imageDrw;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public boolean isParent() {
        return parent;
    }

    public boolean isSwiped() {
        return swiped;
    }
}
