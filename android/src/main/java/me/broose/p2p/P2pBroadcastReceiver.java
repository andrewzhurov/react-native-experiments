package me.broose.p2p;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;


public class P2pBroadcastReceiver extends BroadcastReceiver {
    private Context context;
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

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.
            showShort("Got broadcast: connection state changed");

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
            //        .findFragmentById(R.id.frag_list);
            //fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
            //        WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
            showShort("Got broadcast: THIS_DEVICE_CHANGED");
        }
    }

    private void showShort(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }

}
