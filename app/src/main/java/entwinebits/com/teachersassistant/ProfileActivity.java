package entwinebits.com.teachersassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;

/**
 * Created by Nargis Rahman on 2/9/2017.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ProfileActivity";
    private FrameLayout user_profile_toolbar_back;
    private ImageView user_profile_edit_iv;
    private TextView user_profile_toolbar_title;

    private UserProfileDTO mUserProfileDTO;
    private boolean isMyProfile;
    private TextView profile_mobile_tv, profile_email_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        if (getIntent().hasExtra(Constants.USER_PROFILE_DTO)) {
            mUserProfileDTO = getIntent().getParcelableExtra(Constants.USER_PROFILE_DTO);
            isMyProfile = false;
        } else {
            mUserProfileDTO = new UserProfileDTO();
            mUserProfileDTO.setUserEmail("myemail@email.com");
            mUserProfileDTO.setUserMobilePhone("01724 257563");
            isMyProfile = true;
        }
        initToolbar();
        initLayout();

        getUser
    }

    private void initLayout() {
        profile_mobile_tv = (TextView) findViewById(R.id.profile_mobile_tv);
        profile_mobile_tv.setOnClickListener(this);
        profile_email_tv = (TextView) findViewById(R.id.profile_email_tv);
        profile_email_tv.setOnClickListener(this);

        profile_mobile_tv.setText(mUserProfileDTO.getUserMobilePhone());
        profile_email_tv.setText(mUserProfileDTO.getUserEmail());
    }

    private void initToolbar() {
        user_profile_toolbar_back = (FrameLayout) findViewById(R.id.user_profile_toolbar_back);
        user_profile_toolbar_back.setOnClickListener(this);
        user_profile_toolbar_title = (TextView) findViewById(R.id.user_profile_toolbar_title);
        user_profile_edit_iv = (ImageView) findViewById(R.id.user_profile_edit_iv);
        user_profile_edit_iv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.profile_mobile_tv:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String mob = profile_mobile_tv.getText().toString().trim();
                callIntent.setData(Uri.parse("tel:" + mob));
                startActivity(callIntent);
                break;

            case R.id.profile_email_tv:

                break;
            case R.id.user_profile_edit_iv:
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.user_profile_toolbar_back:
                ProfileActivity.this.finish();
                break;

        }
    }
}
