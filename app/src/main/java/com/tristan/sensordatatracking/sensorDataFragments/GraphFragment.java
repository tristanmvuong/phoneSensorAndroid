package com.tristan.sensordatatracking.sensorDataFragments;

import android.os.AsyncTask;
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

import java.util.List;
import java.util.Map;

public class GraphFragment extends Fragment {
    private static final String TAG = "accelerometerSensor";
    private SensorData sensorData;
    private View rootView;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.graph_fragment, container, false);
        sensorData = (SensorData) LambdaUtil.getFunction(getActivity().getApplicationContext(), SensorData.class);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button = (Button) rootView.findViewById(R.id.getAccelerometerData);
        addListenerToGetAccelerometer();
    }

    public void addListenerToGetAccelerometer() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccelerometerData(0L, System.currentTimeMillis());
                Log.d(TAG, "Success");
            }
        });
    }

    private void getAccelerometerData(Long startDate, Long endDate) {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(endDate);
        try {
            new SensorGetData().execute(dateRange);
        } catch (Exception e) {
            Log.e(TAG, "error trying to get data");
        }
    }

    private class SensorGetData extends AsyncTask<DateRange, Void, Map<String,AccelerometerData>> {
        protected Map<String,AccelerometerData> doInBackground(DateRange...dateRanges) {
            return sensorData.getDataByDateRange(dateRanges[0]);
        }

        protected void onPostExecute(Map<String,AccelerometerData> result) {
            Graph(result);
        }
    }

    private void Graph(Map<String,AccelerometerData> dataPoints) {
        Log.d(TAG, "Graphing");
    }
}
