package com.tristan.sensordatatracking.sensorDataFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.tristan.sensordatatracking.R;
import com.tristan.sensordatatracking.lambda.entity.AccelerometerData;
import com.tristan.sensordatatracking.lambda.entity.DateRange;
import com.tristan.sensordatatracking.lambda.function.SensorData;
import com.tristan.sensordatatracking.lambda.util.LambdaUtil;

import java.util.Map;

public class GraphFragment extends Fragment {
    private static final String TAG = "accelerometerSensor";
    private SensorData sensorData;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.raw_data_fragment, container, false);
        button = (Button) rootView.findViewById(R.id.getAccelerometerData);

        sensorData = (SensorData) LambdaUtil.getFunction(getActivity().getApplicationContext(), SensorData.class);

        addListenerToGetAccelerometer();

        return rootView;
    }

    public void addListenerToGetAccelerometer() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,AccelerometerData> data = getAccelerometerData(0L, System.currentTimeMillis());
                Log.d(TAG, "Success");
            }
        });
    }

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
}
