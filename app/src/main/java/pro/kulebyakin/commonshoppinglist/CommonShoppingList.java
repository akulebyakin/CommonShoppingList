package pro.kulebyakin.commonshoppinglist;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class CommonShoppingList extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
