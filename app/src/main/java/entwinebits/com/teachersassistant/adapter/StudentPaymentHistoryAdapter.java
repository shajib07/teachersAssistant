package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.listener.PaymentUpdateListener;
import entwinebits.com.teachersassistant.model.PaymentDTO;

/**
 * Created by shajib on 12/30/2016.
 */
public class StudentPaymentHistoryAdapter extends RecyclerView.Adapter<StudentPaymentHistoryAdapter.PaymentHistoryHolder> {

    private String TAG = "StudentPaymentHistoryAdapter";
    private Activity mActivity;
    private ArrayList<String> mMonthList;
    private DatabaseRequestHelper db;
    private PaymentUpdateListener paymentUpdateListener;

    public StudentPaymentHistoryAdapter(Activity activity, PaymentUpdateListener listener) {
        this.mActivity = activity;
        getMonthList();
        this.paymentUpdateListener = listener;
    }

    private void getMonthList() {

        mMonthList = new ArrayList<>();
        String[] arr = mActivity.getResources().getStringArray(R.array.months);
        for (int i=0; i < arr.length; i++) {
            mMonthList.add(arr[i]);
        }
        if (db == null) {
            db = new DatabaseRequestHelper(mActivity);
        }
    }

    @Override
    public PaymentHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_history_single_item, parent, false);
        return new PaymentHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(final PaymentHistoryHolder holder, final int position) {

        holder.paid_month_tv.setText(mMonthList.get(position));
        holder.full_paid_cb.setChecked(true);
        holder.paid_amount_tv.setText("1000");


//        holder.full_paid_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                PaymentDTO dto = new PaymentDTO();
//                dto.setPaymentMonth(position+1);
//
//                if (isChecked) {
//                    Toast.makeText(mActivity, "checkd ", Toast.LENGTH_SHORT).show();
//                    dto.setPaymentStatus(1);
//                    paymentUpdateListener.onPaymentUpdate(dto);
//                } else {
//                    dto.setPaymentStatus(2);
//                    paymentUpdateListener.onPaymentUpdate(dto);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public static class PaymentHistoryHolder extends RecyclerView.ViewHolder {
        private TextView paid_month_tv, paid_year_tv, paid_amount_tv;
        private EditText paid_amount_et;
        private CheckBox full_paid_cb;
        private FrameLayout edit_mode_fl;

        public PaymentHistoryHolder(View itemView) {
            super(itemView);
            paid_month_tv = (TextView) itemView.findViewById(R.id.paid_month_tv);
            paid_year_tv = (TextView) itemView.findViewById(R.id.paid_year_tv);
            paid_amount_tv = (TextView) itemView.findViewById(R.id.paid_amount_tv);
            paid_amount_et = (EditText) itemView.findViewById(R.id.paid_amount_et);
            full_paid_cb = (CheckBox) itemView.findViewById(R.id.full_paid_cb);
            edit_mode_fl = (FrameLayout) itemView.findViewById(R.id.edit_mode_fl);
        }
    }
}
