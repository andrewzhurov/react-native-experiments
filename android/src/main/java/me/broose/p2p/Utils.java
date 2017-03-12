package me.broose.p2p;

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
    public static Map deviceToMap(WifiP2pDevice device){
        Map deviceMap = new HashMap(); 
        deviceMap.putString("deviceName", device.deviceName);
        deviceMap.putString("deviceAddress", device.deviceAddress);
        deviceMap.putInt("status", device.status);
        deviceMap.putString("primaryDeviceType", device.primaryDeviceType);
        deviceMap.putString("secondaryDeviceType", device.secondaryDeviceType);
    }
}
