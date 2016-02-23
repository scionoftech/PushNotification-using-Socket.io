/** Copyright 2016 Sai Kumar Yava

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License. **/


package scionoftech.socketservice.Socket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

import scionoftech.socketservice.Activity.MainActivity;
import scionoftech.socketservice.Ipaddress;
import scionoftech.socketservice.SocketApp;

/**
 * Created by sky on 9/2/16.
 */
//***************************************** Socket Service is a Service class to implement socket functionality*********************************//
public class SocketService extends Service {

    //create object of class
    public static SocketService Single;

    //----------------------------interface object------------------//
    private oneventlitsener mCallback;

    //Socket object
    Socket socket;
    private Context context;
    //socket connection status
    public boolean socket_status = false;

    //constructor
    public SocketService() {
    }

    //-----------------------------interface for sending data from server to activity--------------------------//
    public interface oneventlitsener {
        public void setData(String data, String tag);
    }

    //---------------------------------get context from activity to send data to activity----------------------------//
    public void setContext(Context c) {
        this.context = c;
        mCallback = (oneventlitsener) context;
    }

    /* Static 'instance' method */
    public static SocketService getInstance() {
        return Single;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Single = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runSocket();
        Log.d("Start Service", "Started from Service");

        //restart Service when it killed
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Single = null;
        socket_status = false;
        stopSelf();
    }


    //---------***********------------------Run socket for listening to various event-----*******------//
    public void runSocket() {
        //check if socket connected or not
        if (!socket_status) {
            try {
                IO.Options opts = new IO.Options();
                opts.reconnection = true;//try to reconnect socket when it disconnected from server
                opts.reconnectionDelay = 0;//reconnection delay time in milliseconds
                opts.reconnectionAttempts = 5;//no of attempts to reconnect socket
                //opts.forceNew = true;
                socket = IO.socket(Ipaddress.GetIP, opts);//server ip address
                //Methods for listening to events.
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        Log.d("socket id", socket.id());
                        Log.d("Socket Status", "socket connected");
                        socket_status = true;
                        //call main activity method to tell socket connected
                        MainActivity mainActivity = MainActivity.instance();
                        if (mainActivity != null) {
                            mainActivity.Check();
                        }

                    }

                }).on("send_response", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {

                        //***********------------------send data from service to activity----------------********//
                        try {

                            mCallback.setData(args[0].toString(), "send_response");
                            System.out.println("send_response=======>" + args[0].toString());
                        } catch (Exception e) {

                            System.out.println("send_response=======>" + e.toString());
                        }
                    }
                }).on("notification", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        try {
                            //***********------------------Generate notification----------------********//
                            callBroad(args[0].toString());
                            System.out.println("notification=======>" + args[0].toString());
                        } catch (Exception e) {
                            System.out.println("notification=======>" + e.toString());
                        }
                    }
                }).on(Socket.EVENT_RECONNECT_FAILED, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        Log.d("EVENT_RECONNECT_FAILED", "yes");
                        socket_status = false;
                        Log.d("Socket Status", "socket disconnected");

                    }

                }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        Log.d("EVENT_RECONNECT", "yes");
                        socket_status = true;
                        Log.d("Socket Status", "socket reconnected");

                    }

                }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        Log.d("EVENT_CONNECT_TIMEOUT", "yes");

                    }

                }).on(Socket.EVENT_RECONNECT_ATTEMPT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        Log.d("EVENT_RECONNECT_ATTEMPT", "yes");

                    }

                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        Log.d("EVENT_DISCONNECT", "yes");
                    }

                });
                socket.connect();

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            //call main activity method to tell socket already connected
            MainActivity mainActivity = MainActivity.instance();
            if (mainActivity != null) {
                mainActivity.Check();
            }
        }
    }

    //*********-----------send data to server-----********//
    public void emit(String key, JSONObject data) {
        socket.emit(key, data);

        System.out.println(key + "@@@@@@@@@ emitting");
    }

    public void emit(String key, String data) {
        socket.emit(key, data);
        System.out.println(key + "@@@@@@@@@ emitting");
    }

    public void emit(String key, JSONArray data) {
        socket.emit(key, data);

        System.out.println(key + "@@@@@@@@ emitting");
    }

    //call broadcast receiver to generate notifications
    public void callBroad(String data) {
        SocketApp playforever = SocketApp.getInstance();
        Intent intent = new Intent();
        intent.putExtra("message", data);
        intent.setAction("scionooftech.NOTIFICATION");
        playforever.context.sendBroadcast(intent);
    }
    //-----------------------**********************End of Socket code***************-------------------//
}
