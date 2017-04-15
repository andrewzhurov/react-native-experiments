package me.broose.p2p;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;

import com.facebook.react.bridge.Callback;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import android.widget.Toast;
import java.lang.Math;
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
    
  // Broken
  @ReactMethod
  public void registerService(String instanceName, String serviceType, ReadableMap infoStringPairs) {
      //  Create a string map containing information about your service.
      //Map<String, String> about = Utils.toRNMap(infoStringPairs);
      Map about = new HashMap();
      about.put("listenport", "6666");
      about.put("buddyname", "Dark Guy" + (int) (Math.random() * 1000));
      about.put("available", "visible");

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
                  Utils.sendEvent(reactContext, "service registration success", null);
              }

              @Override
              public void onFailure(int arg0) {
                  // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                  WritableMap cargo = Arguments.createMap();
                  cargo.putInt("reason", arg0);
                  Utils.sendEvent(reactContext, "service registration failure", cargo);
              }
          });
  } 
  
  // BAD
  // Broken
  final HashMap<String, String> buddies = new HashMap<String, String>();
  @ReactMethod
  public void discoverServices() {
      WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
              @Override
              /* Callback includes:
               * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
               * record: TXT record dta as a map of key/value pairs.
               * device: The device running the advertised service.
               */

              public void onDnsSdTxtRecordAvailable(String fullDomain, Map record, WifiP2pDevice device) {
                  log("DnsSdTxtRecord available -" + record.toString());
                  try {
                      // TODO put record inside
                      WritableMap deviceRNMap = me.broose.p2p.Utils.deviceToRNMap(device);
                      WritableMap cargo = Arguments.createMap();
                      cargo.putString("fullDomain", fullDomain);
                      cargo.putString("record", "dull");
                      cargo.putMap("device", deviceRNMap);

                      Utils.sendEvent(reactContext, "txt service record found", cargo);
                      buddies.put(device.deviceAddress, (String) record.get("buddyname"));
                  } catch (Exception e) {
                      log("Exception while obtaining txt record of service");
                  }
              }
          }; 
      WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
              @Override
              public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                  WifiP2pDevice resourceType) {

                  // Update the device name with the human-friendly version from
                  // the DnsTxtRecord, assuming one arrived.
                  resourceType.deviceName = buddies
                      .containsKey(resourceType.deviceAddress) ? buddies
                      .get(resourceType.deviceAddress) : resourceType.deviceName;
                  WritableMap cargo = me.broose.p2p.Utils.deviceToRNMap(resourceType);
                  
                  Utils.sendEvent(reactContext, "service discovered", cargo);
                  log("onBonjourServiceAvailable " + instanceName);
              }
          };

      this.mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);
  } 
    
  @Override
  public String getName() {
    return "P2pAndroid";
  }
    
  private void log(String message) {
      Log.i("broose_react-native-experiments", message);
  }

}
