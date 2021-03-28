package org.envision.parkai;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.aviran.cookiebar2.CookieBar;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    boolean about_hack = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadHomeFragment();
                    return true;
                case R.id.navigation_dashboard:
                    loadHostFragment();
                    return true;
                case R.id.navigation_notifications:
                   loadProfileFragment();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(!isNetworkAvailable())
        {
            // Toast.makeText(MainActivity.this, "Connect to Internet to Download Contents",
            //       Toast.LENGTH_LONG).show();

            CookieBar.build(MainActivity.this)
                    .setTitle("Device not connected to Internet")
                    .setMessage("Please connect to update!")
                    .setDuration(5000)
                    .setBackgroundColor(R.color.red)
                    .show();
        }

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            //startActivity(new Intent(MainActivity.this, LoginAuthCheck.class));

            startActivity(new Intent(MainActivity.this, LoginNumberPlate.class));
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        loadHomeFragment();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }




    private void loadHomeFragment() {
        about_hack = false;
        FragmentBook fragment = FragmentBook.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        //setTitle("Home");
        ft.addToBackStack(null);
        ft.commit();
    }

    private void loadHostFragment() {
        about_hack = false;
        FragmentHost fragment = FragmentHost.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        //setTitle("Home");
        ft.addToBackStack(null);
        ft.commit();
    }

    private void loadProfileFragment() {
        about_hack = false;
        FragmentProfile fragment = FragmentProfile.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        //setTitle("Home");
        ft.addToBackStack(null);
        ft.commit();
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
