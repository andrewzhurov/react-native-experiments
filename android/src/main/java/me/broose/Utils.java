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
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class Utils {

    public static WritableMap toRNMap(@Nullable Map stringMap) {
        if (stringMap == null) {
            return null;
        }

        WritableMap out = Arguments.createMap();
        for (Map.Entry entry : stringMap.entrySet()) {
            final String key = entry.getKey().toString();
            final Object value = entry.getValue();
            if (value instanceof String) {
                out.putString(key, (String) value);
            } else if (value instanceof Integer) {
                // TODO may not work
                out.putInt(key, (int) value);
            } 
        }
        return out;
    }

    public static void sendEvent(ReactContext reactContext,
                                 String eventName,
                                 @Nullable WritableMap params) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }

}
