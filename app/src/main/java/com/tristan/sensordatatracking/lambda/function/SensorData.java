package com.tristan.sensordatatracking.lambda.function;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.tristan.sensordatatracking.lambda.entity.AccelerometerData;
import com.tristan.sensordatatracking.lambda.entity.DateRange;
import com.tristan.sensordatatracking.lambda.entity.Result;

import java.util.List;

public interface SensorData {
    @LambdaFunction(functionName="sensor_save")
    Result save(AccelerometerData data);

    @LambdaFunction(functionName="sensor_read")
    List<AccelerometerData> getDataByDateRange(DateRange dateRange);
}
