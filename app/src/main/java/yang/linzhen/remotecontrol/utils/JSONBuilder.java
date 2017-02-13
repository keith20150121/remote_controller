package yang.linzhen.remotecontrol.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/23.
 */
public class JSONBuilder {

    public static final String KEY_JSON_DEVICE = "KEY_JSON_DEVICE";
    public static final String KEY_MOUSE_ACTION = "KEY_MOUSE_ACTION";
    public static final String KEY_KEYBOARD_ACTION = "KEY_KEYBOARD_ACTION";

    public static final int DEVICE_KEYBOARD     = 0b0001;
    public static final int DEVICE_MOUSE        = 0b0010;

    public static JSONObject create(int device) {

        JSONObject object = new JSONObject();
        try {
            object.put(KEY_JSON_DEVICE, device);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static class MouseAction {
        public static final String KEY_POINT = "KEY_POINT";
        public static final String KEY_ACTION = "KEY_ACTION";


        public static final int MOUSEEVENTF_MOVE        = 0x00000001;
        public static final int MOUSEEVENTF_LEFTDOWN    = 0x00000002;
        public static final int MOUSEEVENTF_LEFTUP      = 0x00000004;
        public static final int MOUSEEVENTF_RIGHTDOWN   = 0x00000008;
        public static final int MOUSEEVENTF_RIGHTUP     = 0x00000010;
        public static final int MOUSEEVENTF_MIDDLEDOWN  = 0x00000020;
        public static final int MOUSEEVENTF_MIDDLEUP    = 0x00000040;
        public static final int MOUSEEVENTF_ABSOLUTE    = 0x00008000;

        public static final int MOUSEEVENTF_STARTTOUCH  = 0x00010000;

        public static JSONObject create(int action, Vector2 point) {
            try {
                JSONObject pointObject = new JSONObject();
                pointObject.put("x", point.x);
                pointObject.put("y", point.y);
                return new JSONObject().put(KEY_ACTION, action).put(KEY_POINT, pointObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
