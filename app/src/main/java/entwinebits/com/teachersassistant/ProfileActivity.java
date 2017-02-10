package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nargis Rahman on 2/9/2017.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ProfileActivity";
    private FrameLayout user_profile_toolbar_back;
    private ImageView user_profile_edit_iv;
    private TextView user_profile_toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_layout);
        initToolbar();

    }

    private void initToolbar() {
        user_profile_toolbar_back = (FrameLayout)findViewById(R.id.user_profile_toolbar_back);
        user_profile_toolbar_back.setOnClickListener(this);
        user_profile_toolbar_title = (TextView) findViewById(R.id.user_profile_toolbar_title);
        user_profile_edit_iv = (ImageView) findViewById(R.id.user_profile_edit_iv);
        user_profile_edit_iv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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
