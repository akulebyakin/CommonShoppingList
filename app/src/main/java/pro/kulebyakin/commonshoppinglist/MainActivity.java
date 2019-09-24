package pro.kulebyakin.commonshoppinglist;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import pro.kulebyakin.commonshoppinglist.fragments.DashboardFragment;
import pro.kulebyakin.commonshoppinglist.fragments.HomeFragment;
import pro.kulebyakin.commonshoppinglist.fragments.NotificationsFragment;
import pro.kulebyakin.commonshoppinglist.fragments.TestOneFragment;
import pro.kulebyakin.commonshoppinglist.fragments.TestTwoFragment;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = new DashboardFragment();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = new NotificationsFragment();
                    break;
                case R.id.navigation_test1:
                    selectedFragment = new TestOneFragment();
                    break;
                case R.id.navigation_test2:
                    selectedFragment = new TestTwoFragment();
                    break;
            }

            // Вывести фрагмент
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}
