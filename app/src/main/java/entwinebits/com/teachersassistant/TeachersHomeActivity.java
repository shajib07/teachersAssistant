package entwinebits.com.teachersassistant;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import entwinebits.com.teachersassistant.adapter.ViewPagerAdapter;
import entwinebits.com.teachersassistant.fragment.PaymentHistoryFragment;
import entwinebits.com.teachersassistant.fragment.TeachersHomeFragment;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class TeachersHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "TeachersHomeActivity";
    private FrameLayout home_toolbar_add;

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private SmoothActionBarDrawerToggle mDrawerToggle;

    private LinearLayout overflow_ll;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        checkFirstTime();
        setContentView(R.layout.activity_teachers_home);

        initLayout();
        setUpHeaderView();
        onMenuItemSelected();
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }


    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        mDrawerToggle = new SmoothActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        overflow_ll = (LinearLayout)findViewById(R.id.overflow_ll);
        overflow_ll.setOnClickListener(this);

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
                switch (item.getItemId()) {
                    case R.id.drawer_home:
                        break;

                    case R.id.drawer_profile:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TeachersHomeActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                            }
                        });
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.drawer_payment_history:

                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                //Intent intent = new Intent(TeachersHomeActivity.this, PaymentHistoryActivity.class);
                                Intent intent = new Intent(TeachersHomeActivity.this, PaymentActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                            }
                        });
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.drawer_participated_batches:

                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TeachersHomeActivity.this, ParticipatedBatchActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                            }
                        });
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.drawer_search:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TeachersHomeActivity.this, UserSearchActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                            }
                        });
                        mDrawerLayout.closeDrawer(Gravity.LEFT);

                        break;

                    case R.id.drawer_pending_req:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TeachersHomeActivity.this, PendingRequestActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                            }
                        });
                        mDrawerLayout.closeDrawer(Gravity.LEFT);

                        break;

                    case R.id.drawer_settings:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TeachersHomeActivity.this, BatchActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                            }
                        });
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
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
        txtHeaderName.setText(UserProfileHelper.getInstance(this).getUserName());

        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.overflow_ll:
                launchMarket();
                break;
        }
    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;
        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
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
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
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

//    private ActionBarDrawerToggle setUpDrawerToggle() {
//        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                invalidateOptionsMenu();
//            }
//        };
//        return drawerToggle;
//    }

}
