package me.broose.p2p;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
//import javax.security.auth.callback.Callback;
//import java.util.stream.Stream;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;


public class P2pBroadcastReceiver extends BroadcastReceiver {
    private Context context;
    private WifiP2pManager mManager;
    private Channel mChannel;
    // private Callback newPeerListCallback;
    // , Callback newPeerListCallback
    
    public P2pBroadcastReceiver(WifiP2pManager mManager, Channel mChannel) {
        this.mManager = mManager;
        this.mChannel = mChannel;
        //this.newPeerListCallback = newPeerListCallback;
    }

    private PeerListListener peerListListener = new PeerListListener() {
        private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

             // TODO uncomment and fix
            ArrayList<WifiP2pDevice> refreshedPeers = (ArrayList) peerList.getDeviceList();
            if (!refreshedPeers.equals(this.peers)) {
                   this.peers.clear();
                   this.peers.addAll(refreshedPeers);
                   //Stream<String> strStream = this.peers.stream()
                   //                                     .map(WifiP2pDevice::deviceAddress);
                   //public class Dull {
                   //    public static String sumStrings(String a, String b) {
                   //        return a + b;
                   //    }};
                   //String out = strStream.reduce("", Dull::sumStrings);
                   ArrayList<String> addresses = new ArrayList<>();
                   for (WifiP2pDevice device : refreshedPeers){
                       addresses.add(device.deviceAddress);
                   }
                   
                   String out = "";
                   for (String address : addresses) {
                       out += address;
                   }
                   
                   showLong(out);

            }

            if (peers.size() == 0) {
                showLong("new device list is empty");
                return;
            }
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

            showShort("Got broadcast: THIS_DEVICE_CHANGED");
            showLong(String.format("address:%s name:%s status:%s type:%s", device.deviceAddress, device.deviceName, device.status, device.primaryDeviceType));

        }
    }

    private void showShort(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }
    private void showLong(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();
    }

}
