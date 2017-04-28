package com.tristan.sensordatatracking;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.tristan.sensordatatracking.lambda.entity.AccelerometerData;
import com.tristan.sensordatatracking.lambda.entity.DateRange;
import com.tristan.sensordatatracking.lambda.function.SensorData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "accelerometerSensor";
    private SensorData sensorData;
    private Long lastRun;
    private BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        bottomNavView = (BottomNavigationView) findViewById(R.id.bottomNav);

        bottomNavView.setOnNavigationItemSelectedListener(new SensorDataBottomNavListener());

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, 5000000);
        float[] initValues = {0f, 0f, 0f};
        this.lastRun = System.currentTimeMillis();
        //setAccelerometerDataDisplay(initValues);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-west-2:dd481c0b-18f0-4d73-8cc5-77e105909a68", // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(getApplicationContext(),
                Regions.US_WEST_2, credentialsProvider);

        this.sensorData = lambdaInvokerFactory.build(SensorData.class);

        //addListenerToGetAccelerometer();
    }

    private class SensorDataBottomNavListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.raw:
                    // TODO
                    return true;
                case R.id.graph:
                    // TODO
                    return true;
                case R.id.subs:
                    // TODO
                    return true;
            }
            return false;
        }
    }


    public void addListenerToGetAccelerometer() {
        /*
        Button getData = (Button) findViewById(R.id.getAccelerometerData);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,AccelerometerData> data = getAccelerometerData(0L, System.currentTimeMillis());
                Log.d(TAG, "Success");
            }
        });
        */
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        setAccelerometerDataDisplay(event.values);
    }

    private void setAccelerometerDataDisplay(float [] values) {
        /*
        TextView accelerometerSensorData = (TextView) findViewById(R.id.accelerometerSensorData);
        String data = "x: " + values[0] + " y: " + values[1] + " z: " + values[2];
        accelerometerSensorData.setText(data);

        if (System.currentTimeMillis() - lastRun >= 4500) {
            AccelerometerData accelerometerData = new AccelerometerData();
            accelerometerData.setX(values[0]);
            accelerometerData.setY(values[1]);
            accelerometerData.setZ(values[2]);
            accelerometerData.setDate(System.currentTimeMillis());
            try {
                sensorData.save(accelerometerData);
            } catch (Exception e) {
                Log.e(TAG, "error trying to save the data");
            }
            lastRun = System.currentTimeMillis();
        }
        */
    }

    /*
    private Map<String,AccelerometerData> getAccelerometerData(Long startDate, Long endDate) {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(endDate);
        try {
            return sensorData.getDataByDateRange(dateRange);
        } catch (Exception e) {
            Log.e(TAG, "error trying to get data");
            return null;
        }
    }
    */
}