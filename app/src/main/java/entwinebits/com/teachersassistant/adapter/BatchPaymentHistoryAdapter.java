package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.BatchDTO;

/**
 * Created by shajib on 12/10/2016.
 */
public class BatchPaymentHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;
    private ArrayList<BatchDTO> mBatchList;

    public BatchPaymentHistoryAdapter(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_history_single_item, null);
        return new BatchHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private static class BatchHistoryHolder extends RecyclerView.ViewHolder {
        public BatchHistoryHolder(View itemView) {
            super(itemView);
        }
    }
}
