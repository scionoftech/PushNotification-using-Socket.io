package scionoftech.socketservice.Message;

import android.content.Context;
import android.content.Intent;

/**
 * Created by sky on 9/2/16.
 */
public class CallNotificationBroad {

    public CallNotificationBroad(Context context, String data) {
        Intent intent = new Intent();
        intent.putExtra("message", data);
        intent.setAction("com.kshediv.playforever.NOTIFICATION");
        context.sendBroadcast(intent);
    }
}
