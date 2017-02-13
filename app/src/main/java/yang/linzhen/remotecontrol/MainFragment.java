package yang.linzhen.remotecontrol;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import yang.linzhen.remotecontrol.utils.Logger;

/**
 * Created by Administrator on 2016/6/13.
 */
public class MainFragment extends Fragment {

    private TextView mIpText;
    private TextView mPortText;
    private Button mConnectBtn;
    private EditText mDataText;

    public MainFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_layout, container, false);

        mIpText = (TextView) rootView.findViewById(R.id.ipaddr_text);
        mPortText = (TextView) rootView.findViewById(R.id.port_text);

        mDataText = (EditText) rootView.findViewById(R.id.send_data);

        mConnectBtn = (Button) rootView.findViewById(R.id.connect_btn);
        mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                String ip = preferences.getString(PrefsActivity.KEY_SERVER_IP, "192.168.137.1");
                String portStr = preferences.getString(PrefsActivity.KEY_SERVER_PORT, "8080");

                String[] data = {ip, portStr, mDataText.getText().toString()};
                new SendTask().execute(data);
            }
        });

        rootView.findViewById(R.id.btn_goto_touch_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), TouchActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public class SendTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                int port = Integer.parseInt(params[1]);
                byte[] dataToSend = params[2].getBytes();
                InetAddress server = InetAddress.getByName(params[0]);
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket output = new DatagramPacket(dataToSend, dataToSend.length, server, port);
                socket.send(output);
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
