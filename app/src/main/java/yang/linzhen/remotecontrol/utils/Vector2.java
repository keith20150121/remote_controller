package yang.linzhen.remotecontrol.utils;

/**
 * Created by Administrator on 2016/6/6.
 */
public class Vector2 {
    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this(0, 0);
    }

    public void setPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setOrigin() {
        this.x = 0.0f;
        this.y = 0.0f;
    }
}
