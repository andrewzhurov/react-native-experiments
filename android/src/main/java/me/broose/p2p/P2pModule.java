package me.broose.p2p;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager.Channel;


import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;

import me.broose.p2p.P2pBroadcastReceiver;
import me.broose.MyToastModule;

public class P2pModule extends ReactContextBaseJavaModule {

  private final IntentFilter intentFilter = new IntentFilter();
  private Context context;
  private WifiP2pManager mManager;
  private Channel mChannel;
  private BroadcastReceiver receiver;


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
      context = reactContext;

      mManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
      mChannel= mManager.initialize(context, reactContext.getMainLooper(), null);

  }
    
    @ReactMethod
  public void registerP2pReceiver() {
    // TODO check if unregisterReceiver somehow modify stroke below, if no - put outside
    //receiver = new P2PBroadcastReceiver(mManager, mChannel, this);
    receiver = new P2pBroadcastReceiver();
    context.registerReceiver(receiver, intentFilter);

    showShort("Receiver registered");
  }

 @ReactMethod
  public void unregisterP2pReceiver() {
    context.unregisterReceiver(receiver);

    showShort("Receiver unregistered");
 }

  @ReactMethod
  public void discoverPeers() {
    mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

        @Override
        public void onSuccess() {
            // Code for when the discovery initiation is successful goes here.
            // No services have actually been discovered yet, so this method
            // can often be left blank.  Code for peer discovery goes in the
            // onReceive method, detailed below.
            showShort("Peer discovery success");
        }

        @Override
        public void onFailure(int reasonCode) {
            // Code for when the discovery initiation fails goes here.
            // Alert the user that something went wrong.
            showShort("Peer discovery failure");
        }
        });
  }
 

  
    
  @Override
  public String getName() {
    return "P2pAndroid";
  }
    
  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    return constants;
  }
    
  @ReactMethod
  public void show(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }
    
  private void showShort(String message) {
    Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_SHORT).show();
  }
    
}
