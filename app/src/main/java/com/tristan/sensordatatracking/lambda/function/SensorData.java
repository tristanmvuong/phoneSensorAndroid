package com.tristan.sensordatatracking.lambda.function;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.tristan.sensordatatracking.lambda.entity.AccelerometerData;

public interface SensorData {
    @LambdaFunction(functionName="sensor_save")
    Integer save(AccelerometerData data);
}
