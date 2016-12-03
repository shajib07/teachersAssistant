package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.BatchDTO;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class BatchListAdapter extends RecyclerView.Adapter<BatchListAdapter.BatchViewHolder> {

    private Activity mActivity;
    private ArrayList<BatchDTO> mBatchDTOList;

    public BatchListAdapter(Activity activity, ArrayList<BatchDTO> batchDTOList) {
        this.mActivity = activity;
        this.mBatchDTOList = batchDTOList;
    }

    public void notifyAdapter(ArrayList<BatchDTO> list) {
        if (mBatchDTOList == null) {
            mBatchDTOList = new ArrayList<>();
        }
        mBatchDTOList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public BatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_list_single_item, null);
        return new BatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BatchViewHolder holder, int position) {
        BatchDTO batchDTO = mBatchDTOList.get(position);
        holder.batch_name_tv.setText(batchDTO.getBatchName());
        holder.batch_schedule_tv.setText("11 AM");
    }

    @Override
    public int getItemCount() {
        if (mBatchDTOList != null) {
            return mBatchDTOList.size();
        }
        return 0;
    }

    public class BatchViewHolder extends RecyclerView.ViewHolder {
        private TextView batch_name_tv, batch_schedule_tv;
        public BatchViewHolder(View itemView) {
            super(itemView);
            batch_name_tv = (TextView)itemView.findViewById(R.id.batch_name_tv);
            batch_schedule_tv = (TextView)itemView.findViewById(R.id.batch_schedule_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}