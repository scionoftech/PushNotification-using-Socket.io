package scionoftech.socketservice.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import scionoftech.socketservice.R;
import scionoftech.socketservice.Socket.SocketService;


/**
 * A simple {@link Fragment} subclass.
 */
public class Options extends Fragment {


    public Options() {
        // Required empty public constructor
    }


    SocketService socketService = SocketService.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        Button get_notification = (Button) view.findViewById(R.id.get_notification);
        Button send_notification_all = (Button) view.findViewById(R.id.send_notification_all);
        Button settings = (Button) view.findViewById(R.id.settings);

        socketService.setContext(getActivity());


        final EditText input = (EditText) view.findViewById(R.id.input);

        get_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketService.emit("send", input.getText().toString());
            }
        });
        send_notification_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketService.emit("send_all", input.getText().toString());
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.container, new Settings()).commit();
            }
        });
        return view;
    }

}
