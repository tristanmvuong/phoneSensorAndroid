package com.tristan.sensordatatracking.lambda.function;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.tristan.sensordatatracking.lambda.entity.AccelerometerData;
import com.tristan.sensordatatracking.lambda.entity.Result;

public interface SensorData {
    @LambdaFunction(functionName="sensor_save")
    Result save(AccelerometerData data);
}
