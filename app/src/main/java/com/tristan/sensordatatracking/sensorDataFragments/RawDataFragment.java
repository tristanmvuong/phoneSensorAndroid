package com.tristan.sensordatatracking.sensorDataFragments;

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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.tristan.sensordatatracking.R;
import com.tristan.sensordatatracking.lambda.entity.AccelerometerData;
import com.tristan.sensordatatracking.lambda.function.SensorData;
import com.tristan.sensordatatracking.lambda.util.LambdaUtil;

import static android.content.Context.SENSOR_SERVICE;

public class RawDataFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "accelerometerSensor";
    private SensorData sensorData;
    private Long lastRun;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.raw_data_fragment, container, false);
        textView = (TextView) rootView.findViewById(R.id.accelerometerSensorData);

        lastRun = System.currentTimeMillis();
        float[] initValues = {0f, 0f, 0f};

        sensorData = (SensorData) LambdaUtil.getFunction(getActivity().getApplicationContext(), SensorData.class);

        return rootView;
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
                sensorData.save(accelerometerData);
            } catch (Exception e) {
                Log.e(TAG, "error trying to save the data");
            }
            lastRun = System.currentTimeMillis();
        }
    }
}