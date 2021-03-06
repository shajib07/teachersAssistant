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

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;

/**
 * Created by shajib on 1/3/2017.
 */
public class EditPaymentHistoryAdapter extends RecyclerView.Adapter<EditPaymentHistoryAdapter.PaymentHistoryHolder> {

    private Activity mActivity;
    private ArrayList<String> mMonthList;
    private ArrayList<PaymentHistoryDTO> mHistoryDTOs;

    public EditPaymentHistoryAdapter(Activity activity) {
        this.mActivity = activity;
        getMonthList();
        setupHistoryList();
    }

    private void setupHistoryList() {
        mHistoryDTOs = new ArrayList<>();
        for (int i=0; i<12; i++) {
            PaymentHistoryDTO dto = new PaymentHistoryDTO();
            dto.setMonth(i);
            dto.setPaid(false);
            mHistoryDTOs.add(dto);
        }
    }

    @Override
    public PaymentHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_history_single_item, parent, false);
        return new PaymentHistoryHolder(view);
    }

    private void getMonthList() {
        mMonthList = new ArrayList<>();
        String[] arr = mActivity.getResources().getStringArray(R.array.months);
        for (int i=0; i < arr.length; i++) {
            mMonthList.add(arr[i]);
        }
    }

    public ArrayList<PaymentHistoryDTO> getHistoryDTOs() {
        return mHistoryDTOs == null ? new ArrayList<PaymentHistoryDTO>() : mHistoryDTOs;
    }

    @Override
    public void onBindViewHolder(final PaymentHistoryHolder holder, final int position) {
        PaymentHistoryDTO dto = mHistoryDTOs.get(position);
        holder.paid_month_tv.setText("" + dto.getMonth());
        if (dto.isPaid()) {
            holder.full_paid_cb.setChecked(true);
            holder.others_paid_cb.setChecked(false);
        } else {
            holder.full_paid_cb.setChecked(false);
            holder.others_paid_cb.setChecked(true);
        }
        holder.paid_amount_et.setText(""+ dto.getPaidAmount());
        holder.paid_amount_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mHistoryDTOs.get(position).setPaidAmount(Integer.parseInt(holder.paid_amount_et.getText().toString()));
                    notifyDataSetChanged();
                }
            }
        });

        holder.full_paid_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.paid_amount_et.setEnabled(true);
                    holder.others_paid_cb.setChecked(false);
                    mHistoryDTOs.get(position).setPaid(true);
                    notifyDataSetChanged();
                }
            }
        });

        holder.others_paid_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.paid_amount_et.setEnabled(false);
                    holder.full_paid_cb.setChecked(false);
                    mHistoryDTOs.get(position).setPaid(false);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistoryDTOs == null ? 0 : mHistoryDTOs.size();
    }

    public static class PaymentHistoryHolder extends RecyclerView.ViewHolder {
        private TextView paid_month_tv, paid_year_tv, paid_amount_tv;
        private EditText paid_amount_et;
        private CheckBox full_paid_cb, others_paid_cb;
        private FrameLayout edit_mode_fl;

        public PaymentHistoryHolder(View itemView) {
            super(itemView);
            paid_month_tv = (TextView) itemView.findViewById(R.id.paid_month_tv);
            paid_year_tv = (TextView) itemView.findViewById(R.id.paid_year_tv);
            paid_amount_tv = (TextView) itemView.findViewById(R.id.paid_amount_tv);
            paid_amount_et = (EditText) itemView.findViewById(R.id.paid_amount_et);
            full_paid_cb = (CheckBox) itemView.findViewById(R.id.full_paid_cb);
            others_paid_cb = (CheckBox) itemView.findViewById(R.id.others_paid_cb);
            edit_mode_fl = (FrameLayout) itemView.findViewById(R.id.edit_mode_fl);

            paid_year_tv.setVisibility(View.GONE);
            paid_amount_et.setVisibility(View.VISIBLE);
            paid_amount_et.setEnabled(false);
            paid_amount_tv.setVisibility(View.GONE);
        }
    }
}
