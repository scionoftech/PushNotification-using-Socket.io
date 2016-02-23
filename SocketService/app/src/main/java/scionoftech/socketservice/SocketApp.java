package scionoftech.socketservice;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sky on 9/2/16.
 */
public class SocketApp extends Application {


    private static SocketApp mInstance;
    public Context context;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mInstance = this;
        context = this;
    }

    public static synchronized SocketApp getInstance() {
        return mInstance;
    }

}
