package yang.linzhen.remotecontrol;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/6/13.
 */
public class CustomDrawerActivity extends AppCompatActivity {

    protected FrameLayout mMainView;
    protected DrawerLayout mDrawerContainer;
    protected ListView mDrawerContent;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected final String[] mDrawerItems = {"Preference"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainView = (FrameLayout) findViewById(R.id.main_fragment);
        mDrawerContainer = (DrawerLayout) findViewById(R.id.container_drawer);
        mDrawerContent = (ListView) findViewById(R.id.drawer_content);
        mDrawerContent.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerItems));

        initDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void initDrawer() {

        mDrawerContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mDrawerContainer.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), PrefsActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerContainer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        mDrawerContainer.addDrawerListener(mDrawerToggle);
        mDrawerContainer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mMainView.setTranslationX(drawerView.getWidth() * slideOffset);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            mDrawerContainer.post(new Runnable() {
                @Override
                public void run() {
                    supportInvalidateOptionsMenu();
                }
            });
        }
        return true;
    }
}
