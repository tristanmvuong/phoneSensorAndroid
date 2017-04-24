package com.tristan.sensordatatracking;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.tristan.sensordatatracking.lambda.function.SensorData;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "accelerometerSensor";
    private SensorData sensorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, 2000000);
        float[] initValues = {0f, 0f, 0f};
        setAccelerometerDataDisplay(initValues);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-west-2:dd481c0b-18f0-4d73-8cc5-77e105909a68", // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(getApplicationContext(),
                Regions.US_WEST_2, credentialsProvider);

        this.sensorData = lambdaInvokerFactory.build(SensorData.class);
    }



    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.8f;

        float [] gravity = new float[3];

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        float [] linear_acceleration = new float[3];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        setAccelerometerDataDisplay(linear_acceleration);
    }

    private void setAccelerometerDataDisplay(float [] values) {
        TextView proximitySensorData = (TextView) findViewById(R.id.proximitySensorData);
        String data = "x: " + values[0] + " y: " + values[1] + " z: " + values[2];
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            System.out.println("loggable");
        } else {
            System.out.println("not loggable");
        }
        Log.d(TAG, data);
        proximitySensorData.setText(data);
    }
}
