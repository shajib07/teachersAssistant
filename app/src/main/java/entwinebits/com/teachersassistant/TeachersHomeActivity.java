package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import entwinebits.com.teachersassistant.adapter.ViewPagerAdapter;
import entwinebits.com.teachersassistant.fragment.PaymentHistoryFragment;
import entwinebits.com.teachersassistant.fragment.TeachersHomeFragment;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class TeachersHomeActivity extends AppCompatActivity {

    private String TAG = "TeachersHomeActivity";
    private FrameLayout home_toolbar_add;

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private Handler mHandler ;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_home);

        mHandler = new Handler();
        initLayout();
        setUpHeaderView();
        onMenuItemSelected();
    }

    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerToggle = setUpDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TeachersHomeFragment(), "Home");
        adapter.addFragment(new PaymentHistoryFragment(), "Payment History");
        viewPager.setAdapter(adapter);
    }

    private void onMenuItemSelected() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent;
                switch (item.getItemId()) {

                    case R.id.drawer_home:
                        break;

                    case R.id.drawer_courses:
                        intent = new Intent(TeachersHomeActivity.this, BatchActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.drawer_payment_history:
                        intent = new Intent(TeachersHomeActivity.this, PaymentHistoryActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.drawer_more:
                        break;

                    case R.id.drawer_settings:
                        intent = new Intent(TeachersHomeActivity.this, BatchActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    private void setUpHeaderView() {
        View headerView = navigationView.getHeaderView(0);
        TextView txtHeaderName = (TextView) headerView.findViewById(R.id.nav_header_name);
        TextView txtHeaderMobPhone = (TextView) headerView.findViewById(R.id.nav_header_mob_phn);

        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }

    }


    private ActionBarDrawerToggle setUpDrawerToggle() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        return drawerToggle;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }



}
