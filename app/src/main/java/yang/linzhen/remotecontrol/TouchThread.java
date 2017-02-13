package yang.linzhen.remotecontrol;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import yang.linzhen.remotecontrol.utils.JSONBuilder;
import yang.linzhen.remotecontrol.utils.Logger;
import yang.linzhen.remotecontrol.utils.Vector2;

/**
 * Created by Administrator on 2016/6/12.
 */
public class TouchThread extends HandlerThread {

    private final String TAG = "TouchThread";

    private Handler.Callback mMsgCallback;

    private String mServerIp;
    private int mServerPort;

    public String getmServerIp() {
        return mServerIp;
    }

    public void setmServerIp(String mServerIp) {
        this.mServerIp = mServerIp;
    }

    public int getmServerPort() {
        return mServerPort;
    }

    public void setmServerPort(int mServerPort) {
        this.mServerPort = mServerPort;
    }

    private MouseController mMouseController;

    public TouchThread(String name) {
        super(name);

        mMouseController = new MouseController();
        mMsgCallback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Vector2 point = new Vector2();
                if (msg.obj != null) {
                    point = ((Vector2[])msg.obj)[0];
                }
                switch (msg.what) {
                    case TouchActivity.MSG_TOUCH_START:
                        mMouseController.startTouch(point);
                        return true;
                    case TouchActivity.MSG_MOVE:
                        mMouseController.move(point);
                        break;
                    case TouchActivity.MSG_LEFT_BTN_DOWN:
                        mMouseController.leftBtnDown();
                        break;
                    case TouchActivity.MSG_LEFT_BTN_UP:
                        mMouseController.leftBtnUp();
                        break;
//                    case TouchActivity.MSG_DOUBLE_TAP_CONFIRM:
//                        break;
                    case TouchActivity.MSG_DOUBLE_TAP_MOVE:
                        mMouseController.move(point);
                        break;
                    case TouchActivity.MSG_DOUBLE_TAP_START:
                        mMouseController.startDoubleTap(point);
                        break;
                    case TouchActivity.MSG_TOUCH_END:
                        mMouseController.endTouch(point);
                        break;
                    case TouchActivity.MSG_RIGHT_BTN_DOWN:
                        mMouseController.rightBtnDown();
                        break;
                    case TouchActivity.MSG_RIGHT_BTN_UP:
                        mMouseController.rightBtnUp();
                        break;
                    default:
                        break;
                }
                Logger.i("linzhen.yang", "handleMessage: " + msg.what);

                if (isValidJsonObject(mMouseController.getJsonObject()))
                    sendDataToServer();
                return true;
            }
        };
    }

    public Handler.Callback getCallback() { return mMsgCallback; }

    private boolean isValidJsonObject(JSONObject object) {
        try {
            int device = object.getInt(JSONBuilder.KEY_JSON_DEVICE);
            JSONObject mouseAction = object.getJSONObject(JSONBuilder.KEY_MOUSE_ACTION);
            if (mouseAction == null)
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void sendDataToServer() {
        try {
            Logger.i("linzhen.yang", "JSON: " + mMouseController.getJsonObject().toString());
            byte[] data = mMouseController.getJsonObject().toString().getBytes();
            InetAddress server = InetAddress.getByName(mServerIp);
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket output = new DatagramPacket(data, data.length, server, mServerPort);
            socket.send(output);
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}