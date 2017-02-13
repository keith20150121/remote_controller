package yang.linzhen.remotecontrol;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import rx.Observable;
import yang.linzhen.remotecontrol.utils.Vector2;

/**
 * Created by Administrator on 2016/6/21.
 */
public class NetworkService extends Service {

    public final static String EXTRA_IP = "IP";
    public final static String EXTRA_PORT = "PORT";

    private TouchThread mNetThread;
    private Handler mMsgHandler;

    private final IBinder mServiceBinder = new NetworkServiceBinder();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNetThread != null) {
            mNetThread.quitSafely();
            mNetThread = null;
        }
    }

    public class NetworkServiceBinder extends Binder {
        public NetworkService getService() {
            return NetworkService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNetThread = new TouchThread("ActionSender");
        mNetThread.setmServerIp(intent.getStringExtra("IP"));
        mNetThread.setmServerPort(intent.getIntExtra("PORT", 8080));
        mNetThread.start();
        mMsgHandler = new Handler(mNetThread.getLooper(), mNetThread.getCallback());
        return super.onStartCommand(intent, flags, startId);
    }

    public void sendActionMessage(int action, Vector2[] points) {
        if (points != null) {
            mMsgHandler.removeMessages(action);
            mMsgHandler.obtainMessage(action, points).sendToTarget();
        } else {
            mMsgHandler.removeMessages(action);
            mMsgHandler.obtainMessage(action).sendToTarget();
        }
    }

    public void sendActionMessage(int action1, int action2, Vector2[] points) {
        if (points != null) {
            mMsgHandler.removeMessages(action1);
            mMsgHandler.obtainMessage(action1, points).sendToTarget();
            mMsgHandler.sendMessageDelayed(mMsgHandler.obtainMessage(action2, points), 20L);
        }
    }
}
