package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 1/27/2017.
 */
public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SignupActivity";
    private EditText etName, etMobilePhone, etPassword;
    private EditText etMobilePhoneIn, etPasswordIn;
    private String name, mobilePhone, password;
    long mAppUserId;
    private Button btnSignUp, btnSignIn;
    private LinearLayout signUp_ll, signIn_ll;
    private TextView txtSignUp, txtSignIn, skip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_laoyout);
        initLayout();
    }


    private void initLayout() {
        skip = (TextView) findViewById(R.id.skip);
        signUp_ll = (LinearLayout) findViewById(R.id.signUp_ll);
        signIn_ll = (LinearLayout) findViewById(R.id.signIn_ll);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignIn = (TextView) findViewById(R.id.txtSignIn);

        skip.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);

        etMobilePhoneIn = (EditText) findViewById(R.id.etMobilePhoneIn);
        etPasswordIn = (EditText) findViewById(R.id.etPasswordIn);

        etName = (EditText) findViewById(R.id.etName);
        etMobilePhone = (EditText) findViewById(R.id.etMobilePhone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
    }

    private void doSignup() {

        name = etName.getText().toString();
        mobilePhone = etMobilePhone.getText().toString();
        password = etPassword.getText().toString();
        if (name.length() < 1) {
            Toast.makeText(this, "Please input your name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobilePhone.length() < 1) {
            Toast.makeText(this, "Please input your Mobile Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 1) {
            Toast.makeText(this, "Please input your Password", Toast.LENGTH_SHORT).show();
            return;
        } else if (etPassword.getText().length() < 3) {
            Toast.makeText(this, "Password at least 3 digits long", Toast.LENGTH_SHORT).show();
            return;
        }

        sendSignUpRequest();
    }

    private void sendSignUpRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, 0);
            jsonObject.put(ServerConstants.FULL_NAME, name);
            jsonObject.put(ServerConstants.PHONE_NUMBER, mobilePhone);
            jsonObject.put(ServerConstants.PASSWORD, password);
        } catch (Exception e) {
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        HelperMethod.debugLog(TAG, response.toString());
                        try {
                            if (!response.getBoolean(ServerConstants.ERROR)) {
                                mAppUserId = response.getLong(ServerConstants.ID);
                                saveUserId();
                            } else {
                                Toast.makeText(SignupActivity.this, response.getString(ServerConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void saveUserId() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putLong(Constants.APP_USER_ID, mAppUserId);
        editor.putBoolean(Constants.ALREADY_LOGGED_IN, true);
        editor.commit();

        Intent intent = new Intent(SignupActivity.this, TeachersHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendSignInRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ServerConstants.ACTION, 11);
            jsonObject.put(ServerConstants.PHONE_NUMBER, mobilePhone);
            jsonObject.put(ServerConstants.PASSWORD, password);
        }catch (Exception e){}

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        try {
                            if (!response.getBoolean(ServerConstants.ERROR)) {
                                saveUserInfo(response);
                            } else {
                                Toast.makeText(SignupActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);

    }

    private void doSignIn() {
        mobilePhone = etMobilePhoneIn.getText().toString();
        password = etPasswordIn.getText().toString();
        if (mobilePhone.length() < 1) {
            Toast.makeText(this, "Please input your Mobile Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 1) {
            Toast.makeText(this, "Please input your Password", Toast.LENGTH_SHORT).show();
            return;
        } else if (etPasswordIn.getText().length() < 3) {
            Toast.makeText(this, "Password at least 3 digits long", Toast.LENGTH_SHORT).show();
            return;
        }

        sendSignInRequest();
    }

    private void saveUserInfo(JSONObject json) {

        try {
            if (json.has("users")) {
                HelperMethod.debugLog(TAG, "saveUserInfo inside if");
                JSONArray jsonArray = json.getJSONArray("users");
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                mAppUserId = jsonObject.getLong(ServerConstants.ID);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putLong(Constants.APP_USER_ID, mAppUserId);
                editor.putBoolean(Constants.ALREADY_LOGGED_IN, true);
                editor.commit();

                Intent intent = new Intent(SignupActivity.this, TeachersHomeActivity.class);
                startActivity(intent);
                HelperMethod.debugLog(TAG, "after inside if");

                finish();

            }

        } catch (Exception e) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                doSignup();
                break;

            case R.id.btnSignIn:

                doSignIn();
                break;

            case R.id.txtSignUp:
                signIn_ll.setVisibility(View.GONE);
                signUp_ll.setVisibility(View.VISIBLE);
                txtSignIn.setVisibility(View.VISIBLE);
                txtSignUp.setVisibility(View.GONE);
                break;

            case R.id.txtSignIn:
                signIn_ll.setVisibility(View.VISIBLE);
                signUp_ll.setVisibility(View.GONE);
                txtSignIn.setVisibility(View.GONE);
                txtSignUp.setVisibility(View.VISIBLE);

                break;

            case R.id.skip:

//                Toast.makeText(this, "skip", Toast.LENGTH_SHORT).show();
//                finish();
//                Intent intent = new Intent(SignupActivity.this, TeachersHomeActivity.class);
//                startActivity(intent);
                break;

//            case R.id.btnSignUp
        }
    }
}
