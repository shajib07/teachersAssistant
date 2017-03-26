package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.utils.Months;

/**
 * Created by shajib on 12/30/2016.
 */
public class StudentPaymentHistoryAdapter extends RecyclerView.Adapter<StudentPaymentHistoryAdapter.PaymentHistoryHolder> {

    private String TAG = "StudentPaymentHistoryAdapter";
    private Activity mActivity;
    private ArrayList<String> mMonthList;
    private ArrayList<PaymentHistoryDTO> mPaymentHistoryList;

    public StudentPaymentHistoryAdapter(Activity activity, ArrayList<PaymentHistoryDTO> list) {
        this.mActivity = activity;
        getMonthList();
        this.mPaymentHistoryList = list;
    }

    private void getMonthList() {
        mMonthList = new ArrayList<>();
        String[] arr = mActivity.getResources().getStringArray(R.array.months);
        for (int i=0; i < arr.length; i++) {
            mMonthList.add(arr[i]);
        }
    }

    @Override
    public PaymentHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_payment_single_item, parent, false);
        return new PaymentHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(final PaymentHistoryHolder holder, final int position) {

        PaymentHistoryDTO dto = mPaymentHistoryList.get(position);
        holder.payment_item_month.setText("" + Months.get(dto.getMonth()));
        if (dto.isPaid()) {
            holder.itemView.setBackgroundColor(mActivity.getResources().getColor(R.color.paid_row_color));
        }
        holder.payment_item_paid.setText(dto.getPaidAmount() + "");
        holder.payment_item_due.setText("0");
    }

    @Override
    public int getItemCount() {
        return mPaymentHistoryList == null ? 0 : mPaymentHistoryList.size();
    }

    public static class PaymentHistoryHolder extends RecyclerView.ViewHolder {
        private TextView payment_item_month, payment_item_paid, payment_item_due;

        public PaymentHistoryHolder(View itemView) {
            super(itemView);
            payment_item_month = (TextView) itemView.findViewById(R.id.payment_item_month);
            payment_item_paid = (TextView) itemView.findViewById(R.id.payment_item_paid);
            payment_item_due = (TextView) itemView.findViewById(R.id.payment_item_due);
        }
    }
}
