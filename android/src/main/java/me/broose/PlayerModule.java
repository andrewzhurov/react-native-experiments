package me.broose;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;
import android.content.Context;
import java.io.File;
import android.net.Uri;

public class PlayerModule extends ReactContextBaseJavaModule {

  public PlayerModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }
    
  @Override
  public String getName() {
    return "PlayerAndroid";
  }
  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    return constants;
  }
 
  @ReactMethod
  public void create(String path) {
    File file = new File(path);
    Uri uri = Uri.fromFile(file); 
    Toast.makeText(getReactApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
    //this.mp = MediaPlayer.create(getReactApplicationContext(), uri);
  }
  @ReactMethod
  public void start() {
    //mp.start();
  }
  @ReactMethod
  public void pause() {
    //mp.pause();
  }

}
