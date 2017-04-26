package com.tristan.sensordatatracking.lambda.function;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.tristan.sensordatatracking.lambda.entity.AccelerometerData;
import com.tristan.sensordatatracking.lambda.entity.DateRange;
import com.tristan.sensordatatracking.lambda.entity.Result;

import java.util.List;
import java.util.Map;

public interface SensorData {
    @LambdaFunction(functionName="sensor_save")
    Result save(AccelerometerData data);

    @LambdaFunction(functionName="sensor_read")
    Map<String,AccelerometerData> getDataByDateRange(DateRange dateRange);
}
