package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;

/**
 * Created by shajib on 1/14/2017.
 */
public class BatchPaymentAdapter extends RecyclerView.Adapter<BatchPaymentAdapter.BatchPaymentHolder> {

    private ArrayList<PaymentHistoryDTO> mPaymentHistoryList;

    public BatchPaymentAdapter(Activity activity, ArrayList<PaymentHistoryDTO> list) {
        this.mPaymentHistoryList = list;
    }

    @Override
    public BatchPaymentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_payment_single_item, parent, false);
        return new BatchPaymentHolder(view);
    }

    @Override
    public void onBindViewHolder(BatchPaymentHolder holder, int position) {

        PaymentHistoryDTO dto = mPaymentHistoryList.get(position);

        holder.payment_item_name.setText(dto.getStudentName());
        holder.payment_item_paid.setText("" + dto.getPaidAmount());
        holder.payment_item_due.setText("0");
    }

    @Override
    public int getItemCount() {
        return mPaymentHistoryList == null ? 0 : mPaymentHistoryList.size();
    }

    public class BatchPaymentHolder extends RecyclerView.ViewHolder {
        private TextView payment_item_name, payment_item_paid, payment_item_due;
        public BatchPaymentHolder(View itemView) {
            super(itemView);
            payment_item_name = (TextView) itemView.findViewById(R.id.payment_item_name);
            payment_item_paid = (TextView) itemView.findViewById(R.id.payment_item_paid);
            payment_item_due = (TextView) itemView.findViewById(R.id.payment_item_due);
        }
    }
}
