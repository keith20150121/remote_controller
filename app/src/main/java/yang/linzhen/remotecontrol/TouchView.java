package yang.linzhen.remotecontrol;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import yang.linzhen.remotecontrol.utils.Logger;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import yang.linzhen.remotecontrol.utils.TouchCallback;
import yang.linzhen.remotecontrol.utils.Vector2;

/**
 * Created by Administrator on 2016/6/5.
 */
public class TouchView extends View {

    private static final String TAG = "TouchView";

    private GestureDetectorCompat mGestureDetector;

    private boolean mStartDoubleTap = false;

    public TouchView(Context context) {
        this(context, null, 0);
    }

    public TouchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
        mLastPoint = new Vector2(0, 0);
    }

    private Vector2 mLastPoint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mTouchCallback.onTouchEnd();
                if (mStartDoubleTap)
                    mStartDoubleTap = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Vector2 point = new Vector2(event.getX(), event.getY());
                if (Math.abs(point.x - mLastPoint.x) <= 0.5f && Math.abs(point.y - mLastPoint.y) <= 0.5f) break;
                if (!mStartDoubleTap) {
                    mTouchCallback.onMove(new Vector2(event.getX(), event.getY()));
                } else {
                    mTouchCallback.onDoubleTapMove(new Vector2(event.getX(), event.getY()));
                }
                mLastPoint = point;
                Logger.i(TAG, "onMove");
            default:
                break;
        }

        return true;
    }

    private TouchCallback mTouchCallback = null;
    public void setTouchCallback(TouchCallback callback) {
        mTouchCallback = callback;
    }

    private GestureDetector.SimpleOnGestureListener mGestureListener = new
        GestureDetector.SimpleOnGestureListener() {

            private boolean mIsDoubleClicked = false;
            private final static int CANCEL_DOUBLE_TAP = 0;
//            private final static int CANCEL_DOUBLE_CLICK = 1;

            private Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case CANCEL_DOUBLE_TAP:
                            mIsDoubleClicked = false;
                            break;
                        default:
                            break;
                    }
                    super.handleMessage(msg);
                }
            };

            @Override
            public boolean onDown(MotionEvent e) {
                mLastPoint.setPoint(e.getX(), e.getY());
                mTouchCallback.onTouchStart(mLastPoint);
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Logger.i(TAG, "onDoubleTapEvent: " + e.getAction());
                switch (e.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (mIsDoubleClicked) {
                            mTouchCallback.onDoubleClickConfirm(new Vector2(e.getX(), e.getY()));
//                            handler.sendMessageDelayed(handler.obtainMessage(CANCEL_DOUBLE_CLICK), 20L);
                        }
                    default:
                        break;
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                mTouchCallback.onRightClick(new Vector2(e.getX(), e.getY()));
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Logger.i(TAG, "onDoubleTap");
                mTouchCallback.onDoubleClickStart(new Vector2(e.getX(), e.getY()));
                mStartDoubleTap = true;
                mIsDoubleClicked = true;
                handler.sendMessageDelayed(handler.obtainMessage(CANCEL_DOUBLE_TAP), 150L);
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Logger.i(TAG, "onSingleTapConfirmed");
                mTouchCallback.onClick(new Vector2(e.getX(), e.getY()));
                return true;
            }

        };

}
