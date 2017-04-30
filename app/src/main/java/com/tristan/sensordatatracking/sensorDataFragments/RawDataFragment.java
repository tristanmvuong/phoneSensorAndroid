package com.tristan.sensordatatracking.sensorDataFragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tristan.sensordatatracking.R;
import com.tristan.sensordatatracking.lambda.entity.AccelerometerData;
import com.tristan.sensordatatracking.lambda.function.SensorData;
import com.tristan.sensordatatracking.lambda.util.LambdaUtil;

import static android.content.Context.SENSOR_SERVICE;

public class RawDataFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "accelerometerSensor";
    private SensorData sensorData;
    private Long lastRun;
    private View rootView;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.raw_data_fragment, container, false);
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 5000000);
        lastRun = System.currentTimeMillis();
        sensorData = (SensorData) LambdaUtil.getFunction(getActivity().getApplicationContext(), SensorData.class);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView = (TextView) rootView.findViewById(R.id.accelerometerSensorData);
        float[] initValues = {0f, 0f, 0f};
        setAccelerometerDataDisplay(initValues);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        setAccelerometerDataDisplay(event.values);
    }

    private void setAccelerometerDataDisplay(float [] values) {
        String data = "x: " + values[0] + " y: " + values[1] + " z: " + values[2];
        textView.setText(data);

        if (System.currentTimeMillis() - lastRun >= 4500) {
            AccelerometerData accelerometerData = new AccelerometerData();
            accelerometerData.setX(values[0]);
            accelerometerData.setY(values[1]);
            accelerometerData.setZ(values[2]);
            accelerometerData.setDate(System.currentTimeMillis());
            try {
                SensorSave sensorSave = new SensorSave(accelerometerData);
                new Thread(sensorSave).start();
            } catch (Exception e) {
                Log.e(TAG, "error trying to save the data");
            }
            lastRun = System.currentTimeMillis();
        }
    }

    private class SensorSave implements Runnable {
        AccelerometerData accelerometerData;

        SensorSave(AccelerometerData accelerometerData) {
            this.accelerometerData = accelerometerData;
        }

        public void run() {
            sensorData.save(accelerometerData);
        }
    }
}