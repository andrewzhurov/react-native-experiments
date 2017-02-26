package me.broose.p2p;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.net.wifi.p2p.WifiP2pDevice;
import javax.security.auth.callback.Callback;
import android.widget.Toast;


public class P2pBroadcastReceiver extends BroadcastReceiver {
    private Context context;
    private WifiP2pManager mManager;
    private Channel mChannel;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private Callback newPeerListCallback;
    
    public P2pBroadcastReceiver(WifiP2pManager mManager, Callback newPeerListCallback) {
        this.mManager = mManager;
        this.newPeerListCallback = newPeerListCallback;
    }

    private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            List<WifiP2pDevice> refreshedPeers = peerList.getDeviceList();
            if (!refreshedPeers.equals(this.peers)) {
                //String out = refreshedPeers.
                // and call cb with new peers
                this.peers.clear();
                this.peers.addAll(refreshedPeers);
                showLong(String.format("device list obj:%s", this.peers.toString));


                // If an AdapterView is backed by this data, notify it
                // of the change.  For instance, if you have a ListView of
                // available peers, trigger an update.
                //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();

                // Perform any other updates needed based on the new list of
                // peers connected to the Wi-Fi P2P network.
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
            Log.d(P2pBroadcastReceiver.TAG, "P2P peers changed");

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
