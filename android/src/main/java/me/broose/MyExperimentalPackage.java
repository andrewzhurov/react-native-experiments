package me.broose;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Map;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import me.broose.MyToastModule;
import me.broose.PlayerModule;
import me.broose.LightSensorModule;


public class MyExperimentalPackage implements ReactPackage {

  @Override
  public List<Class<? extends JavaScriptModule>> createJSModules() {
    return Collections.emptyList();
  }

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    return Collections.emptyList();
  }

  @Override
  public List<NativeModule> createNativeModules(
                              ReactApplicationContext reactContext) {
    List<NativeModule> modules = new ArrayList<>();

    modules.add(new MyToastModule(reactContext));
    modules.add(new PlayerModule(reactContext));
    modules.add(new LightSensorModule(reactContext));

    return modules;
  }
}
