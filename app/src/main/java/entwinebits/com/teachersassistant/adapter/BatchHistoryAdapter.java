package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.PaymentDTO;

/**
 * Created by shajib on 12/10/2016.
 */
public class BatchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;
    private ArrayList<BatchDTO> mBatchList;
    private ArrayList<PaymentDTO> mPaymentList;

    public BatchHistoryAdapter(Activity activity, ArrayList<PaymentDTO> list) {
        this.mActivity = activity;
        this.mPaymentList = list;
//        getPaymentList();
    }

    private void getPaymentList() {
        if (mPaymentList == null) {
            mPaymentList = new ArrayList<>();
        } else {
            mPaymentList.clear();
        }
        PaymentDTO dto;
        int base = 100;
        for (int i =0; i < 20; i++) {

            dto = new PaymentDTO();
            dto.setBatchName("English");
            dto.setTotalAmount(1000+base);
            dto.setTotalPaid(700+base);
            dto.setTotalDue(300);
            base += 500;
            mPaymentList.add(dto);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_history_single_item, null);
        return new BatchHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PaymentDTO dto = mPaymentList.get(position);
        BatchHistoryHolder batchHistoryHolder = (BatchHistoryHolder)holder;
        batchHistoryHolder.history_item_batch_name.setText(dto.getBatchName());
        batchHistoryHolder.history_item_total_amount.setText("" + dto.getTotalAmount());
        batchHistoryHolder.history_item_total_paid.setText("" + dto.getTotalPaid());
        batchHistoryHolder.history_item_total_due.setText("" + dto.getTotalDue());
    }

    @Override
    public int getItemCount() {
        return mPaymentList == null ? 0 : mPaymentList.size();
    }

    private static class BatchHistoryHolder extends RecyclerView.ViewHolder {
        public TextView history_item_batch_name, history_item_total_amount, history_item_total_paid, history_item_total_due;
        public BatchHistoryHolder(View itemView) {
            super(itemView);
            history_item_batch_name = (TextView)itemView.findViewById(R.id.history_item_batch_name);
            history_item_total_amount = (TextView)itemView.findViewById(R.id.history_item_total_amount);
            history_item_total_paid = (TextView)itemView.findViewById(R.id.history_item_total_paid);
            history_item_total_due = (TextView)itemView.findViewById(R.id.history_item_total_due);
        }
    }
}
