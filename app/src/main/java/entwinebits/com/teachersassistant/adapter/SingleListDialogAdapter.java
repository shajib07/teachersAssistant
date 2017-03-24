package entwinebits.com.teachersassistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.listener.DateSelectionListener;
import entwinebits.com.teachersassistant.model.ItemDTO;

/**
 * Created by shajib on 3/23/2017.
 */
public class SingleListDialogAdapter extends RecyclerView.Adapter<SingleListDialogAdapter.SingleListHolder> {

    private String TAG = "SingleListDialogAdapter";
    private ArrayList<ItemDTO> mItemList;
    private DateSelectionListener mDateSelectionListener;

    public SingleListDialogAdapter(ArrayList<ItemDTO> list, DateSelectionListener listener) {
        this.mItemList = list;
        this.mDateSelectionListener = listener;
    }

    @Override
    public SingleListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_chooser_single_item, parent, false);
        return new SingleListHolder(view);
    }

    @Override
    public void onBindViewHolder(SingleListHolder holder, int position) {
        final String itemName = mItemList.get(position).getItemName();
        final int itemId = mItemList.get(position).getItemId();
        holder.item_tv.setText(itemName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateSelectionListener.onDateSelected(true, itemName, itemId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public class SingleListHolder extends RecyclerView.ViewHolder {
        private TextView item_tv;

        public SingleListHolder(View itemView) {
            super(itemView);
            item_tv = (TextView) itemView.findViewById(R.id.date_tv);
        }
    }
}
