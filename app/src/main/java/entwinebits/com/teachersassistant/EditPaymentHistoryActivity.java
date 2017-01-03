package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import entwinebits.com.teachersassistant.adapter.EditPaymentHistoryAdapter;

/**
 * Created by shajib on 1/2/2017.
 */
public class EditPaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout edit_history_save_btn, edit_history_toolbar_back;
    private TextView edit_history_toolbar_title;
    private RecyclerView edit_payment_history_rv;
    private EditPaymentHistoryAdapter editPaymentHistoryAdapter;
    private Spinner edit_history_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment_history);


        initToolbar();
        initLayout();

    }

    private void initToolbar() {
        edit_history_save_btn = (FrameLayout) findViewById(R.id.edit_history_save_btn);
        edit_history_save_btn.setOnClickListener(this);
        edit_history_toolbar_back = (FrameLayout) findViewById(R.id.edit_history_toolbar_back);
        edit_history_toolbar_back.setOnClickListener(this);
        edit_history_toolbar_title = (TextView) findViewById(R.id.edit_history_toolbar_title);
        edit_history_toolbar_title.setText("Edit");
    }

    private void initLayout() {
        editPaymentHistoryAdapter = new EditPaymentHistoryAdapter(this);
        edit_payment_history_rv = (RecyclerView) findViewById(R.id.edit_payment_history_rv);
        edit_payment_history_rv.setLayoutManager(new LinearLayoutManager(this));
        edit_payment_history_rv.setAdapter(editPaymentHistoryAdapter);

        edit_history_spinner = (Spinner) findViewById(R.id.edit_history_spinner);
        String[] years = getResources().getStringArray(R.array.Years);
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, years );
        yearAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        edit_history_spinner.setAdapter(yearAdapter);

        edit_history_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int year = Integer.parseInt(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_history_save_btn:

                break;

            case R.id.edit_history_toolbar_back:
                EditPaymentHistoryActivity.this.finish();
                break;
        }
    }
}
