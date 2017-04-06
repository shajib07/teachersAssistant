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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;
import entwinebits.com.teachersassistant.utils.UserProfileHelper;

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
    private TextView profile_name_tv, profile_email_tv, profile_mobile_tv, profile_city_tv, profile_country_tv, profile_instut_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        if (getIntent().hasExtra(Constants.USER_PROFILE_DTO)) {
            mUserProfileDTO = getIntent().getParcelableExtra(Constants.USER_PROFILE_DTO);
            HelperMethod.debugLog(TAG, "m == "+mUserProfileDTO.getUserFirstName());
            isMyProfile = false;
        } else {
            mUserProfileDTO = new UserProfileDTO();
            isMyProfile = true;
        }
        initToolbar();
        initLayout();


        if (isMyProfile) {
            getMyProfile();
        } else {
            updateUI();
        }
    }

    private void getMyProfile() {

        JSONObject jsonObject = ServerRequestHelper.sendGetUserInfoRequest(UserProfileHelper.getInstance(this).getUserId());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            HelperMethod.debugLog(TAG, response.toString());
                            if (!response.optBoolean(ServerConstants.ERROR)) {
                                mUserProfileDTO = ServerResponseParser.parseGetUserInfoRequest(response);

                                updateUI();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void initLayout() {
        profile_mobile_tv = (TextView) findViewById(R.id.profile_mobile_tv);
        profile_mobile_tv.setOnClickListener(this);
        profile_email_tv = (TextView) findViewById(R.id.profile_email_tv);
        profile_email_tv.setOnClickListener(this);
        profile_name_tv = (TextView) findViewById(R.id.profile_name_tv);
        profile_name_tv.setOnClickListener(this);

        profile_city_tv = (TextView) findViewById(R.id.profile_city_tv);
        profile_country_tv = (TextView) findViewById(R.id.profile_country_tv);
        profile_instut_tv = (TextView) findViewById(R.id.profile_instut_tv);
    }

    private void updateUI() {
        HelperMethod.debugLog(TAG, "updateUI == "+mUserProfileDTO.getUserFirstName());
        profile_name_tv.setText(mUserProfileDTO.getUserFirstName());
        profile_mobile_tv.setText(mUserProfileDTO.getUserMobilePhone());
        profile_email_tv.setText(mUserProfileDTO.getUserEmail());

        profile_city_tv.setText(mUserProfileDTO.getUserCity());
        profile_country_tv.setText(mUserProfileDTO.getUserCountry());
        profile_instut_tv.setText(mUserProfileDTO.getUserInstituteName());
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
                intent.putExtra(Constants.USER_PROFILE_DTO, mUserProfileDTO);
                startActivity(intent);
                break;

            case R.id.user_profile_toolbar_back:
                ProfileActivity.this.finish();
                break;

        }
    }
}
