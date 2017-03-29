package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;

/**
 * Created by shajib on 3/29/2017.
 */
public class BatchPaymentHistoryAdapter extends RecyclerView.Adapter<BatchPaymentHistoryAdapter.BatchPaymentHolder> {

    private String TAG ="PaymentHistoryDetailsAdapter";
    private Activity mActivity;
    private ArrayList<PaymentHistoryDTO> mBatchPaymentList;
    private PaymentHistoryDetailsAdapter.PaymentEditSelectionListener mPaymentEditSelectionListener;

    public BatchPaymentHistoryAdapter(Activity activity, ArrayList<PaymentHistoryDTO> batchPaymentList) {
        this.mActivity = activity;
        this.mBatchPaymentList = batchPaymentList;
    }

    public void setPaymentEditSelectionListener(PaymentHistoryDetailsAdapter.PaymentEditSelectionListener listener) {
        this.mPaymentEditSelectionListener = listener;
    }

    @Override
    public BatchPaymentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_pmnt_history_single_item, parent, false);
        return new BatchPaymentHolder(view);
    }

    @Override
    public void onBindViewHolder(BatchPaymentHolder holder, int position) {
        final PaymentHistoryDTO dto = mBatchPaymentList.get(position);
        holder.batch_pmnt_student_name.setText(dto.getUserName());
        holder.batch_pmnt_paid_amount.setText("" + dto.getPaidAmount());
        holder.batch_pmnt_due_amount.setText("0");

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPaymentEditSelectionListener.onPaymentEdited(dto);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBatchPaymentList == null ? 0 : mBatchPaymentList.size();
    }

    public class BatchPaymentHolder extends RecyclerView.ViewHolder {

        private TextView batch_pmnt_student_name, batch_pmnt_paid_amount, batch_pmnt_due_amount;
        public BatchPaymentHolder(View itemView) {
            super(itemView);
            batch_pmnt_student_name = (TextView)itemView.findViewById(R.id.batch_pmnt_student_name);
            batch_pmnt_paid_amount = (TextView)itemView.findViewById(R.id.batch_pmnt_paid_amount);
            batch_pmnt_due_amount = (TextView)itemView.findViewById(R.id.batch_pmnt_due_amount);
        }
    }
}
