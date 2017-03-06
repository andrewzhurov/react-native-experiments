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
import android.util.Log;

import android.media.MediaPlayer;

public class PlayerModule extends ReactContextBaseJavaModule {
  private static MediaPlayer mp;

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
    log(uri.toString());
    MediaPlayer mp = MediaPlayer.create(getReactApplicationContext(), uri);
    if(this.mp != null) {
      this.mp.release();
    }
    if(mp != null) {
      this.mp = mp;
      log("Player created");
    } else {
      log("Can't create a player");
    }
  }
  @ReactMethod
  public void start() {
    if (this.mp != null) {
    this.mp.start();
    }
  }
  @ReactMethod
  public void pause() {
    if (this.mp != null) {
    this.mp.pause();
    }
  }
  @ReactMethod
  public void stop() {
    if (this.mp != null) {
    this.mp.stop();
    }
  }
  @ReactMethod
  public void prepare() {
    try {
    this.mp.prepare();
    } catch (Exception ex) {
    log("Exception during prepare");
    }
  }

  private void log(String message) {
      Log.i("broose_react-native-experiments", message);
  }
}
