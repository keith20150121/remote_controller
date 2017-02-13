package yang.linzhen.remotecontrol;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import yang.linzhen.remotecontrol.utils.JSONBuilder;
import yang.linzhen.remotecontrol.utils.Logger;
import yang.linzhen.remotecontrol.utils.Vector2;

/**
 * Created by Administrator on 2016/6/14.
 */
public class MouseController {

    public enum STATE {
        MOVE_START,
        MOVE_END,
    }

    private int mCurrentAction = 0;

    private STATE state = STATE.MOVE_END;

    private Vector2 mStartPoint;
    private Vector2 mEndPoint;
    //TODO Add mouse buttons

    private JSONObject jsonObject;

    public MouseController() {

        mStartPoint = new Vector2(0, 0);
        mStartPoint = new Vector2(0, 0);

        jsonObject = JSONBuilder.create(JSONBuilder.DEVICE_MOUSE);
    }

    public Vector2 getStartPoint() {
        return mStartPoint;
    }

    public void setStartPoint(Vector2 startPoint) {
        this.mStartPoint = startPoint;
    }

    public Vector2 getEndPoint() {
        return mEndPoint;
    }

    public void setEndPoint(Vector2 endPoint) {
        this.mEndPoint = endPoint;
    }

    public void startTouch(Vector2 point) {
        if (state == STATE.MOVE_END) {
            state = STATE.MOVE_START;
            setStartPoint(point);
        }
    }

    public void startDoubleTap(Vector2 point) {
        startTouch(point);
        setEndPoint(getStartPoint());
        leftBtnDown();
    }

    public void move(Vector2 point) {
        if (state == STATE.MOVE_START) {
            mCurrentAction |= JSONBuilder.MouseAction.MOUSEEVENTF_MOVE;
            setEndPoint(point);
            try {
                jsonObject.remove(JSONBuilder.KEY_MOUSE_ACTION);
                jsonObject.put(JSONBuilder.KEY_MOUSE_ACTION,
                        JSONBuilder.MouseAction.create(mCurrentAction,
                                new Vector2(
                                        getEndPoint().x - getStartPoint().x,
                                        getEndPoint().y - getStartPoint().y
                                )
                        )
                );
                Logger.i("linzhen.yang", String.format("x: %f, y: %f", getEndPoint().x - getStartPoint().x, getEndPoint().y - getStartPoint().y));
                setStartPoint(point);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void leftBtnDown() {
        mCurrentAction |= JSONBuilder.MouseAction.MOUSEEVENTF_LEFTDOWN;
        mCurrentAction &= ~JSONBuilder.MouseAction.MOUSEEVENTF_LEFTUP;
        try {
            jsonObject.remove(JSONBuilder.KEY_MOUSE_ACTION);
            jsonObject.put(JSONBuilder.KEY_MOUSE_ACTION,
                    JSONBuilder.MouseAction.create(mCurrentAction,
                            new Vector2(0, 0)
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void leftBtnUp() {
        if ((mCurrentAction & JSONBuilder.MouseAction.MOUSEEVENTF_LEFTUP) == JSONBuilder.MouseAction.MOUSEEVENTF_LEFTUP) {
            mCurrentAction &= ~JSONBuilder.MouseAction.MOUSEEVENTF_LEFTUP;
            return;
        }
        if ((mCurrentAction & JSONBuilder.MouseAction.MOUSEEVENTF_LEFTDOWN) == JSONBuilder.MouseAction.MOUSEEVENTF_LEFTDOWN) {
            mCurrentAction |= JSONBuilder.MouseAction.MOUSEEVENTF_LEFTUP;
            mCurrentAction &= ~JSONBuilder.MouseAction.MOUSEEVENTF_LEFTDOWN;
            try {
                jsonObject.remove(JSONBuilder.KEY_MOUSE_ACTION);
                jsonObject.put(JSONBuilder.KEY_MOUSE_ACTION,
                        JSONBuilder.MouseAction.create(mCurrentAction,
                                new Vector2(0, 0)
                        )
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void rightBtnDown() {
        if (state == STATE.MOVE_START &&
                ((mCurrentAction & JSONBuilder.MouseAction.MOUSEEVENTF_LEFTDOWN) == JSONBuilder.MouseAction.MOUSEEVENTF_LEFTDOWN)) {
            return;
        }

        mCurrentAction |= JSONBuilder.MouseAction.MOUSEEVENTF_RIGHTDOWN;
        mCurrentAction &= ~JSONBuilder.MouseAction.MOUSEEVENTF_RIGHTUP;
        try {
            jsonObject.remove(JSONBuilder.KEY_MOUSE_ACTION);
            jsonObject.put(JSONBuilder.KEY_MOUSE_ACTION,
                    JSONBuilder.MouseAction.create(mCurrentAction,
                            new Vector2(0, 0)
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void rightBtnUp() {
        if ((mCurrentAction & JSONBuilder.MouseAction.MOUSEEVENTF_RIGHTUP) == JSONBuilder.MouseAction.MOUSEEVENTF_RIGHTUP) {
            mCurrentAction &= ~JSONBuilder.MouseAction.MOUSEEVENTF_RIGHTUP;
            return;
        }
        if ((mCurrentAction & JSONBuilder.MouseAction.MOUSEEVENTF_RIGHTDOWN) == JSONBuilder.MouseAction.MOUSEEVENTF_RIGHTDOWN)
        {
            mCurrentAction |= JSONBuilder.MouseAction.MOUSEEVENTF_RIGHTUP;
            mCurrentAction &= ~JSONBuilder.MouseAction.MOUSEEVENTF_RIGHTDOWN;
            try {
                jsonObject.remove(JSONBuilder.KEY_MOUSE_ACTION);
                jsonObject.put(JSONBuilder.KEY_MOUSE_ACTION,
                        JSONBuilder.MouseAction.create(mCurrentAction,
                                new Vector2(0, 0)
                        )
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void endTouch(Vector2 point) {
        state = STATE.MOVE_END;
        setEndPoint(point);
        setStartPoint(point);
        leftBtnUp();
        rightBtnUp();
        try {
            jsonObject.remove(JSONBuilder.KEY_MOUSE_ACTION);
            jsonObject.put(JSONBuilder.KEY_MOUSE_ACTION,
                    JSONBuilder.MouseAction.create(mCurrentAction,
                            new Vector2(0, 0)
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
