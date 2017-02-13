package yang.linzhen.remotecontrol.utils;

/**
 * Created by Administrator on 2016/6/8.
 */
public interface TouchCallback {
    void onTouchStart(Vector2 point);
    void onMove(Vector2 point);
    void onClick(Vector2 point);
    void onDoubleTapMove(Vector2 point);
    void onDoubleClickStart(Vector2 point);
    void onDoubleClickConfirm(Vector2 point);
    void onRightClick(Vector2 point);
    void onTouchEnd();
}
