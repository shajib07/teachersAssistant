package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by Nargis Rahman on 2/10/2017.
 */
public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "EditProfileActivity";
    private FrameLayout edit_profile_toolbar_back, edit_profile_save_fl;
    private TextView edit_profile_toolbar_title;
    private EditText user_name_et, user_mobile_et, user_email_et, user_instut_et, user_designation_et, user_city_et, user_country_et;
    private UserProfileDTO mUserProfileDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_layout);
        if (getIntent().hasExtra(Constants.USER_PROFILE_DTO)) {
            mUserProfileDTO = getIntent().getParcelableExtra(Constants.USER_PROFILE_DTO);
        }
        initToolbar();
        initLayout();
    }

    private void initLayout() {
        user_name_et = (EditText)findViewById(R.id.user_name_et);
        user_mobile_et = (EditText)findViewById(R.id.user_mobile_et);
        user_email_et = (EditText)findViewById(R.id.user_email_et);
        user_instut_et = (EditText)findViewById(R.id.user_instut_et);
        user_designation_et = (EditText)findViewById(R.id.user_designation_et);
        user_city_et = (EditText)findViewById(R.id.user_city_et);
        user_country_et = (EditText)findViewById(R.id.user_country_et);

        if (mUserProfileDTO != null) {
            user_name_et.setText(mUserProfileDTO.getUserFirstName() == "" ? getString(R.string.not_set) : mUserProfileDTO.getUserFirstName());
            user_mobile_et.setText(mUserProfileDTO.getUserMobilePhone() == "" ? getString(R.string.not_set) : mUserProfileDTO.getUserMobilePhone());
            user_email_et.setText(mUserProfileDTO.getUserEmail() == "" ? getString(R.string.not_set) : mUserProfileDTO.getUserEmail());
            user_instut_et.setText(mUserProfileDTO.getUserInstituteName() == "" ? getString(R.string.not_set) : mUserProfileDTO.getUserInstituteName());
            user_designation_et.setText(mUserProfileDTO.getUserDesignation() == "" ? getString(R.string.not_set) : mUserProfileDTO.getUserDesignation());
            user_city_et.setText(mUserProfileDTO.getUserCity() == "" ? getString(R.string.not_set) : mUserProfileDTO.getUserCity());
            user_country_et.setText(mUserProfileDTO.getUserCountry() == "" ? getString(R.string.not_set) : mUserProfileDTO.getUserCountry());
        }
    }

    private void sendUpdateUserInfoReq() {

        JSONObject jsonObject = ServerRequestHelper.sendUpdateUserInfoRequest(mUserProfileDTO);
        HelperMethod.debugLog(TAG, "sendUpdateUserInfoReq id == " + mUserProfileDTO.getUserId());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        if (!response.optBoolean(ServerConstants.ERROR)) {

                            Toast.makeText(EditProfileActivity.this, "User Info Updated", Toast.LENGTH_SHORT).show();
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

    private UserProfileDTO getUpdateUserInfo() {

        UserProfileDTO dto = new UserProfileDTO();
        boolean isChanged = false;
        if (!mUserProfileDTO.getUserFirstName().equalsIgnoreCase(user_name_et.getText().toString()))
        {
            dto.setUserFirstName(user_name_et.getText().toString());
            isChanged = true;
        }
        if (!mUserProfileDTO.getUserMobilePhone().equalsIgnoreCase(user_mobile_et.getText().toString()))
        {
            dto.setUserMobilePhone(user_mobile_et.getText().toString());
            isChanged = true;
        }
        if (!mUserProfileDTO.getUserEmail().equalsIgnoreCase(user_email_et.getText().toString()))
        {
            dto.setUserEmail(user_email_et.getText().toString());
            isChanged = true;
        }
        if (!mUserProfileDTO.getUserInstituteName().equalsIgnoreCase(user_instut_et.getText().toString()))
        {
            dto.setUserInstituteName(user_instut_et.getText().toString());
            isChanged = true;
        }
//        if (!mUserProfileDTO.getUserDesignation().equalsIgnoreCase(user_designation_et.getText().toString()))
//        {
//            dto.setUserDesignation(user_name_et.getText().toString());
//            isChanged = true;
//        }
        if (!mUserProfileDTO.getUserCity().equalsIgnoreCase(user_city_et.getText().toString()))
        {
            dto.setUserCity(user_city_et.getText().toString());
            isChanged = true;
        }
        if (!mUserProfileDTO.getUserCountry().equalsIgnoreCase(user_country_et.getText().toString()))
        {
            dto.setUserCountry(user_country_et.getText().toString());
            isChanged = true;
        }

        return isChanged == true ? dto : null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_profile_save_fl:
                UserProfileDTO dto = getUpdateUserInfo();
                if (dto != null)
                {
                    sendUpdateUserInfoReq();
                }
                break;

            case R.id.edit_profile_toolbar_back:
                EditProfileActivity.this.finish();
                break;
        }
    }

    private void initToolbar() {
        edit_profile_toolbar_back = (FrameLayout)findViewById(R.id.edit_profile_toolbar_back);
        edit_profile_toolbar_back.setOnClickListener(this);
        edit_profile_toolbar_title = (TextView) findViewById(R.id.user_profile_toolbar_title);
        edit_profile_save_fl = (FrameLayout) findViewById(R.id.edit_profile_save_fl);
        edit_profile_save_fl.setOnClickListener(this);
    }

}
