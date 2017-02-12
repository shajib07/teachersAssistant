package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nargis Rahman on 2/10/2017.
 */
public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "EditProfileActivity";
    private FrameLayout edit_profile_toolbar_back, edit_profile_save_fl;
    private TextView edit_profile_toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_layout);
        initToolbar();
        initLayout();
    }

    private void initLayout() {

    }

    private void initToolbar() {
        edit_profile_toolbar_back = (FrameLayout)findViewById(R.id.edit_profile_toolbar_back);
        edit_profile_toolbar_back.setOnClickListener(this);
        edit_profile_toolbar_title = (TextView) findViewById(R.id.user_profile_toolbar_title);
        edit_profile_save_fl = (FrameLayout) findViewById(R.id.edit_profile_save_fl);
        edit_profile_save_fl.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_profile_save_fl:

                break;

            case R.id.edit_profile_toolbar_back:
                EditProfileActivity.this.finish();
                break;
        }
    }
}
