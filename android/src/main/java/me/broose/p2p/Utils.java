package me.broose.p2p;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.support.annotation.Nullable;

import android.net.wifi.p2p.WifiP2pDevice;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class Utils {
    public static WritableMap deviceToRNMap(WifiP2pDevice device){
        WritableMap deviceRNMap = Arguments.createMap(); 
        deviceRNMap.putString("deviceName", device.deviceName);
        deviceRNMap.putString("deviceAddress", device.deviceAddress);
        deviceRNMap.putInt("status", device.status);
        deviceRNMap.putString("primaryDeviceType", device.primaryDeviceType);
        deviceRNMap.putString("secondaryDeviceType", device.secondaryDeviceType);
        return deviceRNMap;
    }
}
