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
    Toast.makeText(getReactApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
    MediaPlayer mp = MediaPlayer.create(getReactApplicationContext(), uri);
    if(this.mp != null) {
      this.mp.release();
    }
    if(mp != null) {
      this.mp = mp;
      Toast.makeText(getReactApplicationContext(), "Player created", Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(getReactApplicationContext(), "Can't create a player", Toast.LENGTH_LONG).show();
    }
  }
  @ReactMethod
  public void start() {
    this.mp.start();
  }
  @ReactMethod
  public void pause() {
    this.mp.pause();
  }
  @ReactMethod
  public void stop() {
    this.mp.stop();
  }

}
