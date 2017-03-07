package me.broose.p2p;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;

import com.facebook.react.bridge.Callback;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;

import me.broose.p2p.P2pBroadcastReceiver;
import me.broose.Utils;

public class P2pModule extends ReactContextBaseJavaModule {

  private final IntentFilter intentFilter = new IntentFilter();
  private WifiP2pManager mManager;
  private Channel mChannel;
  private BroadcastReceiver receiver;
  private ReactApplicationContext reactContext;


  public P2pModule(ReactApplicationContext reactContext) {
    super(reactContext);
      //  Indicates a change in the Wi-Fi P2P status.
      intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
      // Indicates a change in the list of available peers.
      intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
      // Indicates the state of Wi-Fi P2P connectivity has changed.
      intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
      // Indicates this device's details have changed.
      intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
      this.reactContext = reactContext;

      mManager = (WifiP2pManager) reactContext.getSystemService(Context.WIFI_P2P_SERVICE);
      mChannel = mManager.initialize(reactContext, reactContext.getMainLooper(), null);
      receiver = new P2pBroadcastReceiver(mManager, mChannel, this.reactContext);
  }
    
  @ReactMethod
  public void registerP2pReceiver() {
    // TODO may construct multiple instances (?)
    reactContext.registerReceiver(receiver, intentFilter);

    log("Receiver registered");
  }
  
  @ReactMethod
  public void unregisterP2pReceiver() {
    reactContext.unregisterReceiver(this.receiver);

    log("Receiver unregistered");
  }

  private WifiP2pManager.ActionListener gimmeListener(final String onSucessMsg, final String onFailureMsg){
      return new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {
          log(onSucessMsg);
        }
        @Override
        public void onFailure(int reasonCode) {
          log(onFailureMsg + reasonCode);
        }
      };
  }

  @ReactMethod
  public void createGroup() {
    mManager.createGroup(mChannel,
                         gimmeListener("Group creation success", "Group creation failure"));
  }

  @ReactMethod
  public void removeGroup() {
    mManager.removeGroup(mChannel,
                         gimmeListener("Group removal success", "Group removal failure"));
  }
  @ReactMethod
  public void discoverPeers() {
    mManager.discoverPeers(mChannel,
                           gimmeListener("Peer discovery success", "Group discovery failure"));
  }
    
  @ReactMethod
  public void registerService(String instanceName, String serviceType, ReadableMap infoStringPairs) {
      //  Create a string map containing information about your service.
      Map<String, String> about = Utils.toStringMap(infoStringPairs);

      // Service information.  Pass it an instance name, service type
      // _protocol._transportlayer , and the map containing
      // information other devices will want once they connect to this one.
      WifiP2pDnsSdServiceInfo serviceInfo =
          WifiP2pDnsSdServiceInfo.newInstance(instanceName, serviceType, about);

      // Add the local service, sending the service info, network channel,
      // and listener that will be used to indicate success or failure of
      // the request.
      this.mManager.addLocalService(this.mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
              @Override
              public void onSuccess() {
                  // Command successful! Code isn't necessarily needed here,
                  // Unless you want to update the UI or add logging statements.
                  Utils.sendEvent(this.reactContext, "service registration success", null);
              }

              @Override
              public void onFailure(int arg0) {
                  // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                  WritableMap cargo = Arguments.createMap();
                  cargo.putInt("reason", arg0);
                  Utils.sendEvent(this.reactContext, "service registration failure", cargo);
              }
          });
  } 
    
  @Override
  public String getName() {
    return "P2pAndroid";
  }
    
  private void log(String message) {
      Log.i("broose_react-native-experiments", message);
  }

}
