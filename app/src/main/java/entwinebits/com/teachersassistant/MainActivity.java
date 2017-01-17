//package entwinebits.com.teachersassistant;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.NavigationView;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//
//import android.support.v7.widget.Toolbar;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
//import entwinebits.com.teachersassistant.adapter.ViewPagerAdapter;
//import entwinebits.com.teachersassistant.fragment.PaymentHistoryFragment;
//import entwinebits.com.teachersassistant.fragment.TeachersHomeFragment;
//
///**
// * Created by Nargis Rahman on 12/1/2016.
// */
//public class TeachersHomeActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private String TAG = "TeachersHomeActivity";
//    private Toolbar toolbar;
//    private FrameLayout home_toolbar_add;
//
//    private ActionBarDrawerToggle drawerToggle;
//    private DrawerLayout mDrawerLayout;
//    private NavigationView navigationView;
//    private View navHeader;
//    private TextView txtHeaderName, txtHeaderMobPhn;
//
//    private TabLayout mTabLayout;
//    private ViewPager mViewPager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_teachers_home);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        initLayout();
//    }
//
//    private void initLayout() {
//        navigationView = (NavigationView) findViewById(R.id.navigation_view);
//
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerToggle = setupDrawerToggle();
//
//        // Tie DrawerLayout events to the ActionBarToggle
//        mDrawerLayout.addDrawerListener(drawerToggle);
//
//        drawerToggle.syncState();
//
//        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(mViewPager);
//        mTabLayout = (TabLayout) findViewById(R.id.tabs);
//        mTabLayout.setupWithViewPager(mViewPager);
//
//        navHeader = navigationView.getHeaderView(0);
//        txtHeaderName = (TextView) navHeader.findViewById(R.id.nav_header_name);
//        txtHeaderMobPhn = (TextView) navHeader.findViewById(R.id.nav_header_mob_phn);
//
//        setUpNavigationView();
//    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new TeachersHomeFragment(), "Home");
//        adapter.addFragment(new PaymentHistoryFragment(), "Payment History");
//        viewPager.setAdapter(adapter);
//    }
//
//    @Override
//    public void onClick(View view) {
//
//    }
//
//    private void setUpNavigationView() {
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                mDrawerLayout.closeDrawers();
//                Intent intent;
//                switch (menuItem.getItemId()) {
//
//                    case R.id.drawer_home:
//                        break;
//
//                    case R.id.drawer_courses:
//                        intent = new Intent(TeachersHomeActivity.this, BatchActivity.class);
//                        startActivity(intent);
//                        break;
//
//                    case R.id.drawer_payment_history:
//                        intent = new Intent(TeachersHomeActivity.this, PaymentHistoryActivity.class);
//                        startActivity(intent);
//                        break;
//
//                    case R.id.drawer_more:
//                        break;
//
//                    case R.id.drawer_settings:
//                        intent = new Intent(TeachersHomeActivity.this, BatchActivity.class);
//                        startActivity(intent);
//                        break;
//
//                    default:
//                        break;
//                }
//                //Checking if the item is in checked state or not, if not make it in checked state
////                if (menuItem.isChecked()) {
////                    menuItem.setChecked(false);
////                } else {
////                    menuItem.setChecked(true);
////                }
////                menuItem.setChecked(true);
//
////                loadHomeFragment();
//                return true;
//
//            }
//        });
//
//    }
//
//
//    private ActionBarDrawerToggle setupDrawerToggle() {
//        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
//        // and will not render the hamburger icon without it.
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
//                R.string.drawer_open, R.string.drawer_close) {
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
//                super.onDrawerClosed(drawerView);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
//                super.onDrawerOpened(drawerView);
//            }
//        };
//        return actionBarDrawerToggle;
//    }
//
//}
