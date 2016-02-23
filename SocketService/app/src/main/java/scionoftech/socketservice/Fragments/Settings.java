package scionoftech.socketservice.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import scionoftech.socketservice.Db.DataHandler;
import scionoftech.socketservice.Fragments.Options;
import scionoftech.socketservice.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    public Settings() {
        // Required empty public constructor
    }

    JSONObject settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        try {
            Switch notifications = (Switch) view.findViewById(R.id.notiication_on_off);
            final Switch tone_on_off = (Switch) view.findViewById(R.id.tone_on_off);
            final Switch vibrate_on_off = (Switch) view.findViewById(R.id.vibrate_on_off);
            final Switch light_on_off = (Switch) view.findViewById(R.id.light_on_off);

            final DataHandler dataHandler = new DataHandler(getActivity());
            dataHandler.open();

            Cursor cursor = dataHandler.ReturnNotifications();
            settings = new JSONObject();
            if (cursor.moveToFirst()) {

                settings = new JSONObject(cursor.getString(1));

            }
            dataHandler.close();

            //set stored settings
            if (settings.get("status").toString().equals("on")) {
                notifications.setChecked(true);
                if (settings.get("tone").toString().equals("on")) {
                    tone_on_off.setChecked(true);
                } else {
                    tone_on_off.setChecked(false);
                }
                if (settings.get("vibrate").toString().equals("on")) {
                    vibrate_on_off.setChecked(true);
                } else {
                    vibrate_on_off.setChecked(false);
                }

                if (settings.get("light").toString().equals("on")) {
                    light_on_off.setChecked(true);
                } else {
                    light_on_off.setChecked(false);
                }
            } else {
                notifications.setChecked(false);
                tone_on_off.setChecked(false);
                tone_on_off.setEnabled(false);
                vibrate_on_off.setChecked(false);
                vibrate_on_off.setEnabled(false);
                light_on_off.setChecked(false);
                light_on_off.setEnabled(false);
            }


            notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        tone_on_off.setChecked(true);
                        tone_on_off.setEnabled(true);
                        vibrate_on_off.setChecked(true);
                        vibrate_on_off.setEnabled(true);
                        light_on_off.setChecked(true);
                        light_on_off.setEnabled(true);
                        settings = new JSONObject();
                        try {
                            settings.put("status", "on");
                            settings.put("tone", "on");
                            settings.put("vibrate", "on");
                            settings.put("light", "on");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dataHandler.open();
                        dataHandler.UpdateNotifications(settings.toString());
                        dataHandler.close();
                    } else {
                        tone_on_off.setEnabled(false);
                        tone_on_off.setChecked(false);
                        vibrate_on_off.setChecked(false);
                        vibrate_on_off.setEnabled(false);
                        light_on_off.setChecked(false);
                        light_on_off.setEnabled(false);

                        settings = new JSONObject();
                        try {
                            settings.put("status", "off");
                            settings.put("tone", "off");
                            settings.put("vibrate", "off");
                            settings.put("light", "off");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dataHandler.open();
                        dataHandler.UpdateNotifications(settings.toString());
                        dataHandler.close();
                    }
                }
            });

            tone_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked) {

                            settings.put("tone", "on");

                        } else {
                            settings.put("tone", "off");
                        }
                        dataHandler.open();
                        dataHandler.UpdateNotifications(settings.toString());
                        dataHandler.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            vibrate_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked) {

                            settings.put("vibrate", "on");

                        } else {
                            settings.put("vibrate", "off");
                        }
                        dataHandler.open();
                        dataHandler.UpdateNotifications(settings.toString());
                        dataHandler.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            light_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked) {

                            settings.put("light", "on");

                        } else {
                            settings.put("light", "off");
                        }
                        dataHandler.open();
                        dataHandler.UpdateNotifications(settings.toString());
                        dataHandler.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            FrameLayout back = (FrameLayout) view.findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().replace(R.id.container, new Options()).commit();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

}
