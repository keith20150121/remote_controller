package yang.linzhen.remotecontrol;

import android.app.FragmentManager;
import android.os.Bundle;

public class MainActivity extends CustomDrawerActivity {

    private static final String TAG = "MainActivity";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getFragmentManager();
        mFragmentManager.beginTransaction().
                replace(R.id.main_fragment, new MainFragment(), "MainFragment").commit();
    }

}
