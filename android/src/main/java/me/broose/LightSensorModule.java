package me.broose;

import android.widget.Toast;
import android.content.Context;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Map;
import java.util.HashMap;


public class LightSensorModule extends ReactContextBaseJavaModule {
  private Context context;

  public LightSensorModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.context = reactContext;
  }
    
  @Override
  public String getName() {
    return "LightSensorAndroid";
  }
    
  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    return constants;
  }
  @ReactMethod
  public void toastQuick (String message) {
      Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_SHORT).show();
  }
    
  @ReactMethod
  public void keepMeUpdated() {
      SensorManager mySensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        
      Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
      if(LightSensor != null){
          mySensorManager.registerListener(
                                           LightSensorListener, 
                                           LightSensor, 
                                           SensorManager.SENSOR_DELAY_NORMAL);
          
          toastQuick("Listener registered");
      }else{
      toastQuick("Light sensor doesn't present");}
  }
    
  private final SensorEventListener LightSensorListener
      = new SensorEventListener(){
              @Override
              public void onAccuracyChanged(Sensor sensor, int accuracy) {
                  // TODO Auto-generated method stub
                  
              }
              
              @Override
              public void onSensorChanged(SensorEvent event) {
                  if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                      toastQuick("LIGHT: " + event.values[0]);
                  }
              }
     
    };
}


