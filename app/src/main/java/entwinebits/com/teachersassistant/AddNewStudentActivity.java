package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Calendar;

/**
 * Created by shajib on 12/18/2016.
 */
public class AddNewStudentActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AddNewStudentActivity";
    private FrameLayout add_student_toolbar_back, add_student_save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);
    }

    private void initToolbar() {
        add_student_toolbar_back = (FrameLayout) findViewById(R.id.add_student_toolbar_back);
        add_student_toolbar_back.setOnClickListener(this);

        add_student_save_btn = (FrameLayout) findViewById(R.id.add_student_save_btn);
        add_student_save_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_student_save_btn:

                break;

            case R.id.add_student_toolbar_back:
                AddNewStudentActivity.this.finish();
                break;
        }
    }
}
