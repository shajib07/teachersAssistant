package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import entwinebits.com.teachersassistant.adapter.ViewPagerAdapter;
import entwinebits.com.teachersassistant.fragment.PaymentHistoryFragment;
import entwinebits.com.teachersassistant.fragment.TeachersHomeFragment;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class TeachersHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "TeachersHomeActivity";
    private Toolbar toolbar_home_activity;
    private FrameLayout home_toolbar_add;

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_home);

        initLayout();
    }

    private void initLayout() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @Override
    public void onClick(View view) {

    }
}
