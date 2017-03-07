package me.broose;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.support.annotation.Nullable;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;


public class Utils {

    public static Map<String, String> toStringMap(@Nullable ReadableMap readableMap) {
        if (readableMap == null) {
            return null;
        }

        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        if (!iterator.hasNextKey()) {
            return null;
        }

        Map<String, String> result = new HashMap<>();
        while (iterator.hasNextKey()) {
            String name = iterator.nextKey();
            result.put(name, readableMap.getString(name));
        }

        return result;
    }

    public static void sendEvent(ReactContext reactContext,
                                 String eventName,
                                 @Nullable WritableMap params) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }

}
