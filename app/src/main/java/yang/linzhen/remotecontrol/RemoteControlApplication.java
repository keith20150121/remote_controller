package yang.linzhen.remotecontrol;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2016/6/23.
 */
public class RemoteControlApplication extends Application {

    public static SharedPreferences preferences;
    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

}
