package me.broose;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.ReactPackage;

import java.util.Map;
import java.util.List;

import me.broose.ToastModule;
import me.broose.PlayerModule;
import me.broose.LightSensorModule;


public class Package implements ReactPackage {

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

    modules.add(new ToastModule(reactContext));
    modules.add(new PlayerModule(reactContext));
    modules.add(new LightSensorModule(reactContext));

    return modules;
  }
}
