package scionoftech.socketservice.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import scionoftech.socketservice.Db.DataHandler;
import scionoftech.socketservice.Fragments.Options;
import scionoftech.socketservice.R;
import scionoftech.socketservice.Socket.SocketService;

public class MainActivity extends AppCompatActivity implements SocketService.oneventlitsener {

    private static MainActivity inst;

    public static MainActivity instance() {
        return inst;
    }

    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inst = this;
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        //default notification settings
        JSONObject nsettings = new JSONObject();
        try {
            nsettings.put("status", "on");
            nsettings.put("tone", "on");
            nsettings.put("vibrate", "on");
            nsettings.put("light", "on");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //save notification settings in database
        DataHandler dataHandler = new DataHandler(this);

        dataHandler.open();

        //notification settings
        dataHandler.InsertNotifications(nsettings.toString());

        dataHandler.close();

        //start socket service
        Intent i = new Intent(this, SocketService.class);
        startService(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        inst = null;
    }

    //called from socket service after socket connected to server
    public void Check() {
        pDialog.cancel();
        if (inst != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new Options()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are you sure you want to Exit");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // sv();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                finish();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //called from socket service after data received from server
    @Override
    public void setData(String data, String tag) {

        Log.d("Data from Service", data);

    }
}
