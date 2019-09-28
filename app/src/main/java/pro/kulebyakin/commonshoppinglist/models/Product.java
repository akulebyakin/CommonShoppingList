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

}
