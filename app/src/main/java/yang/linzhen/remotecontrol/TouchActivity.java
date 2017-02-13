package yang.linzhen.remotecontrol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.schedulers.*;
import yang.linzhen.remotecontrol.utils.Logger;
import yang.linzhen.remotecontrol.utils.TouchCallback;
import yang.linzhen.remotecontrol.utils.Vector2;

/**
 * Created by Administrator on 2016/6/5.
 */
public class TouchActivity extends FragmentActivity implements ServiceConnection{

    private TouchView mTouchView;
    private TextView mActionText;
    private TextView mCoordText;

    private TouchCallback mTouchCallback;
    private TouchFragment mTouchFragment;

    private NetworkService mNetworkService;
    private Intent mServiceIntent;

    public static final int MSG_TOUCH_START = 0x01;
    public static final int MSG_MOVE = 0x02;
    public static final int MSG_DOUBLE_TAP_START = 0x03;
    public static final int MSG_DOUBLE_TAP_MOVE = 0x04;
//    public static final int MSG_DOUBLE_TAP_CONFIRM = 0x05;
    public static final int MSG_LEFT_BTN_DOWN = 0x06;
    public static final int MSG_TOUCH_END = 0x08;
    public static final int MSG_LEFT_BTN_UP = 0x09;
    public static final int MSG_RIGHT_BTN_DOWN = 0x0A;
    public static final int MSG_RIGHT_BTN_UP = 0x0B;
    private String mServerIp;
    private int mServerPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        mTouchFragment = new TouchFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(mTouchFragment, "TouchFragment");
        transaction.replace(R.id.touch_frag, mTouchFragment);
        transaction.commit();

        SharedPreferences preferences = RemoteControlApplication.preferences;
        mServerIp = preferences.getString(PrefsActivity.KEY_SERVER_IP, "192.168.173.1");
        String serverPort = preferences.getString(PrefsActivity.KEY_SERVER_PORT, "8080");
        mServerPort = Integer.parseInt(serverPort);

        mServiceIntent =  new Intent(this, NetworkService.class);
        mServiceIntent.putExtra(NetworkService.EXTRA_IP, mServerIp);
        mServiceIntent.putExtra(NetworkService.EXTRA_PORT, mServerPort);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(mServiceIntent);
        bindService(mServiceIntent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNetworkService != null) {
            unbindService(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTouchView = (TouchView) mTouchFragment.getView().findViewById(R.id.touch_view);
        mActionText = (TextView) mTouchFragment.getView().findViewById(R.id.action_desc);
        mCoordText = (TextView) mTouchFragment.getView().findViewById(R.id.coor_desc);

        mTouchCallback = new TouchCallback() {
            @Override
            public void onTouchStart(Vector2 point) {
                mNetworkService.sendActionMessage(MSG_TOUCH_START, new Vector2[] { point });
            }

            @Override
            public void onMove(Vector2 point) {
                mNetworkService.sendActionMessage(MSG_MOVE, new Vector2[] { point });
            }

            @Override
            public void onTouchEnd() {
                mNetworkService.sendActionMessage(MSG_TOUCH_END, null);
            }

            @Override
            public void onDoubleTapMove(Vector2 point) {
                mNetworkService.sendActionMessage(MSG_DOUBLE_TAP_MOVE, new Vector2[] { point });
            }

            @Override
            public void onDoubleClickStart(Vector2 point) {
                mNetworkService.sendActionMessage(MSG_DOUBLE_TAP_START, new Vector2[] { point });
            }

            @Override
            public void onDoubleClickConfirm(Vector2 point) {
//                mNetworkService.sendActionMessage(new int[] { MSG_LEFT_BTN_DOWN, MSG_LEFT_BTN_UP, MSG_LEFT_BTN_DOWN, MSG_LEFT_BTN_UP }, new Vector2[] { point });
            }

            @Override
            public void onRightClick(Vector2 point) {
                mNetworkService.sendActionMessage( MSG_RIGHT_BTN_DOWN, MSG_RIGHT_BTN_UP, new Vector2[] { point });
            }

            @Override
            public void onClick(Vector2 point) {
                mNetworkService.sendActionMessage(MSG_LEFT_BTN_DOWN, MSG_LEFT_BTN_UP, new Vector2[] { point });
            }
        };

        mTouchView.setTouchCallback(mTouchCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mNetworkService = ((NetworkService.NetworkServiceBinder) service).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mNetworkService = null;
    }
}
