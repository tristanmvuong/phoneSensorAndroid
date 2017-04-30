package com.tristan.sensordatatracking;

import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.tristan.sensordatatracking.sensorDataFragments.GraphFragment;
import com.tristan.sensordatatracking.sensorDataFragments.RawDataFragment;
import com.tristan.sensordatatracking.sensorDataFragments.SubFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "accelerometerSensor";
    private BottomNavigationView bottomNavView;
    private RawDataFragment rawDataFragment;
    private GraphFragment graphFragment;
    private SubFragment subFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        bottomNavView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavView.setOnNavigationItemSelectedListener(new SensorDataBottomNavListener());

        rawDataFragment = new RawDataFragment();
        graphFragment = new GraphFragment();
        subFragment = new SubFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_fragmentholder,rawDataFragment)
                .add(R.id.frame_fragmentholder,graphFragment)
                .add(R.id.frame_fragmentholder,subFragment)
                .commit();

        switchFragment(rawDataFragment);
    }

    private class SensorDataBottomNavListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.raw:
                    switchFragment(rawDataFragment);
                    return true;
                case R.id.graph:
                   switchFragment(graphFragment);
                    return true;
                case R.id.subs:
                    switchFragment(subFragment);
                    return true;
            }
            return false;
        }
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(rawDataFragment)
                .hide(graphFragment)
                .hide(subFragment)
                .show(fragment)
                .commit();
    }
}