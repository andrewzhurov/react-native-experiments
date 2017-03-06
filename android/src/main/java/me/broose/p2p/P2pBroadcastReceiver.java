package me.broose.p2p;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

//import java.util.stream.Stream;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;

public class P2pBroadcastReceiver extends BroadcastReceiver {
    private Context context;
    private WifiP2pManager mManager;
    private Channel mChannel;
    public P2pBroadcastReceiver(WifiP2pManager mManager, Channel mChannel) {
        this.mManager = mManager;
        this.mChannel = mChannel;
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }
    private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            WritableMap out = Arguments.createMap();
            ArrayList<WifiP2pDevice> refreshedPeers = new ArrayList<>(peerList.getDeviceList());
            for (WifiP2pDevice device : refreshedPeers){
                out.putString("deviceName", device.deviceName);
                out.putString("deviceAddress", device.deviceAddress);
                out.putString("status", device.status);
                out.putString("primaryDeviceType", device.primaryDeviceType);
            }
            
            sendEvent(reactContext, "new peer list", out);
            }
        };
    
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                showShort("Got broadcast: wifi p2p enabled");
            } else {
                showShort("Got broadcast: wifi p2p disabled");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed!  We should probably do something about
            // that.
            showShort("Got broadcast: peer list has changed");
            
            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (this.mManager != null) {
                this.mManager.requestPeers(this.mChannel, this.peerListListener);
            }
            //Log.d(P2pBroadcastReceiver.TAG, "P2P peers changed");

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.
            showShort("Got broadcast: connection state changed");

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
            //        .findFragmentById(R.id.frag_list);
            //fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
            //        WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
            WifiP2pDevice device = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);

            showLong(String.format("THIS_DEVICE_CHANGED address:%s name:%s status:%s type:%s", device.deviceAddress, device.deviceName, device.status, device.primaryDeviceType));
        }
   }

    private void showShort(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }
    private void showLong(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();
    }

}
