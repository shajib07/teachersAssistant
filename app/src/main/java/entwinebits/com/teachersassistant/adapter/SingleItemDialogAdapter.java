package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.listener.DateSelectionListener;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 1/14/2017.
 */
public class SingleItemDialogAdapter extends RecyclerView.Adapter<SingleItemDialogAdapter.SingleItemHolder> {
    private String TAG = "SingleItemDialogAdapter";
    private ArrayList<String> mItemList;
    private DateSelectionListener mDateSelectionListener;

    public SingleItemDialogAdapter(Activity activity, ArrayList<String> list, DateSelectionListener listener) {
        this.mItemList = list;
        this.mDateSelectionListener = listener;
    }

    @Override
    public SingleItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_chooser_single_item, parent, false);
        return new SingleItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final SingleItemHolder holder, int position) {
        holder.item_tv.setText(mItemList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateSelectionListener.onDateSelected(true, holder.item_tv.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        private TextView item_tv;
        public SingleItemHolder(View itemView) {
            super(itemView);
            item_tv = (TextView) itemView.findViewById(R.id.date_tv);

        }
    }
}
