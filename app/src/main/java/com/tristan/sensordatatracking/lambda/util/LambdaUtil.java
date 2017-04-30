package com.tristan.sensordatatracking.lambda.util;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

public class LambdaUtil {
    public static Object getFunction(Context context, Class<?> clz) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-west-2:dd481c0b-18f0-4d73-8cc5-77e105909a68", // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context,
                Regions.US_WEST_2, credentialsProvider);

        return lambdaInvokerFactory.build(clz);
    }
}
