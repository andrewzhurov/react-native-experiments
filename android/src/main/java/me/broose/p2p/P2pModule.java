package me.broose.p2p;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.facebook.react.bridge.Callback;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;

import me.broose.p2p.P2pBroadcastReceiver;

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
    
  @Override
  public String getName() {
    return "P2pAndroid";
  }
    
  private void log(String message) {
      Log.i("broose_react-native-experiments", message);
  }
}
