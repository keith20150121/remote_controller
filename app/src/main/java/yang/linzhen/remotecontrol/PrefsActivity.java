package yang.linzhen.remotecontrol;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;

/**
 * Created by Administrator on 2016/6/13.
 */
public class PrefsActivity extends AppCompatActivity {

    public static final String TAG = "PrefsActivity";

    public static final String KEY_SERVER_IP = "ip_addr_preference";
    public static final String KEY_SERVER_PORT = "port_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefs_layout);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        MyPrefsFragment myPrefsFragment = new MyPrefsFragment();
        getFragmentManager().beginTransaction().
            replace(R.id.pref_fragment, myPrefsFragment, "MainFragment").commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    public static class MyPrefsFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener{

        SharedPreferences.Editor mEditor;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onStart() {
            super.onStart();

            SharedPreferences preferences = RemoteControlApplication.preferences;
            mEditor = preferences.edit();

            EditTextPreference ipPref = (EditTextPreference) findPreference(KEY_SERVER_IP);
            EditTextPreference portPref = (EditTextPreference) findPreference(KEY_SERVER_PORT);

            setSummaryAndText(ipPref, (preferences.getString(KEY_SERVER_IP, "192.168.173.1")));
            ipPref.setOnPreferenceChangeListener(this);

            String portStr = preferences.getString(KEY_SERVER_PORT, "8080");
            portPref.setOnPreferenceChangeListener(this);
            setSummaryAndText(portPref, portStr);
        }

        private void setSummaryAndText(EditTextPreference preference, String text) {
            preference.setText(text);
            preference.setSummary(preference.getText());
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference instanceof EditTextPreference) {
                String text = (String) newValue;
                if (((String) newValue).isEmpty()) {
                    return false;
                }

                switch (preference.getKey()) {
                    case KEY_SERVER_IP:
                        mEditor.putString(KEY_SERVER_IP, text);
                        setSummaryAndText((EditTextPreference) preference, text);
                        break;
                    case KEY_SERVER_PORT:
                        try {
                            int portNum = Integer.parseInt(text);
                        } catch (Exception e) {
                            Log.e(TAG, "Not number!!");
                            text = ((EditTextPreference) preference).getText();
                        }
                        mEditor.putString(KEY_SERVER_PORT, text);
                        setSummaryAndText((EditTextPreference) preference, text);
                        break;
                    default:
                        break;
                }
                mEditor.apply();
                return true;
            }
            return false;
        }
    }
}
