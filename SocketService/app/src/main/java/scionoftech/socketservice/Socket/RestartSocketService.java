package scionoftech.socketservice.Socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import scionoftech.socketservice.Socket.SocketService;

/**
 * Created by sky on 9/2/16.
 */
public class RestartSocketService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            Intent startServiceIntent = new Intent(context, SocketService.class);
            context.startService(startServiceIntent);
            Log.d("From receiver", "connected");
        } else {
            Log.d("From receiver", "not connected");
            // not connected to the internet
        }

    }
}
