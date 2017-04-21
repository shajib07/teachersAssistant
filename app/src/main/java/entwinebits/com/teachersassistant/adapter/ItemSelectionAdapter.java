package entwinebits.com.teachersassistant.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.interfaces.ItemSelectionType;

/**
 * Created by Nargis Rahman on 4/20/2017.
 */
public class ItemSelectionAdapter extends RecyclerView.Adapter<ItemSelectionAdapter.SelectionViewHolder> {

    private ArrayList<ItemSelectionType> itemDataList;

    private ICenterItemListener iCenterItemListener;
    private RecyclerView  recyclerView;
    public static final int VIEW_TYPE_PADDING = 1;
    public static final int VIEW_TYPE_ITEM = 2;
    private int paddingWidthDate = 0;

    private int selectedItem = -1;

    public ItemSelectionAdapter(ArrayList<ItemSelectionType> dateData, RecyclerView recyclerView,ICenterItemListener listener, int paddingWidthDate) {
        this.itemDataList = dateData;
        this.paddingWidthDate = paddingWidthDate;
        this.recyclerView =recyclerView;
        this.iCenterItemListener = listener;
    }

    public interface ICenterItemListener{
        void onCenterItemChanged(ItemSelectionType selectedItem, RecyclerView recyclerView);
    }

    public void setSelecteditem(int selecteditem) {
        this.selectedItem = selecteditem;
        iCenterItemListener.onCenterItemChanged(itemDataList.get(selecteditem), recyclerView);
        notifyDataSetChanged();
    }
    @Override
    public ItemSelectionAdapter.SelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selection_item_view,
                    parent, false);
            return new SelectionViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selection_item_view,
                    parent, false);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            layoutParams.height = paddingWidthDate;
            view.setLayoutParams(layoutParams);
            return new SelectionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ItemSelectionAdapter.SelectionViewHolder holder, int position) {
        ItemSelectionType labelerDate = itemDataList.get(position);
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            holder.tvDate.setText(labelerDate.getItemName());
            holder.tvDate.setVisibility(View.VISIBLE);

            /*if (position == selectedItem) {
                holder.tvDate.setTextColor(Color.parseColor("#76FF03"));
                holder.tvDate.setTextSize(35);

            } else {
                holder.tvDate.setTextColor(Color.WHITE);
                holder.tvDate.setTextSize(18);
            }*/
        } else {
            holder.tvDate.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ItemSelectionType item = itemDataList.get(position);
        if (item.getItemType() == VIEW_TYPE_PADDING) {
            return VIEW_TYPE_PADDING;
        } else {
            return VIEW_TYPE_ITEM;
        }

    }


    protected class SelectionViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;

        public SelectionViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.txtItemName);
        }
    }
}
