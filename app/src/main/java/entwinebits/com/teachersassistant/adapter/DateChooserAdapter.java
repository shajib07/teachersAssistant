package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.DateSelectionListener;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.utils.Constants;

/**
 * Created by shajib on 12/16/2016.
 */
public class DateChooserAdapter extends RecyclerView.Adapter<DateChooserAdapter.DateViewHolder> {

    private ArrayList<String> mList;
    private DateSelectionListener dateSelectionListener;
    private Activity mActivity;
    private boolean mYear = false;

    public DateChooserAdapter(Activity activity) {
        this.mActivity = activity;
        mList = new ArrayList<>();
        getMonthList();
    }

    private void getMonthList() {
        mYear = false;
        String[] arr = mActivity.getResources().getStringArray(R.array.Months);
        for (int i=0; i < arr.length; i++) {
            mList.add(arr[i]);
        }
    }

    private void getYearList() {
        mYear = true;
        String[] arr = mActivity.getResources().getStringArray(R.array.Years);
        for (int i=0; i < arr.length; i++) {
            mList.add(arr[i]);
        }
    }

    public void setAdapterData(boolean year) {
        mList.clear();
        if (year) {
            getYearList();
        } else {
            getMonthList();
        }
        notifyDataSetChanged();
    }

    public void setDateSelectionListener(DateSelectionListener listener) {
        this.dateSelectionListener = listener;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_chooser_single_item, null);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DateViewHolder holder, int position) {

        holder.date_tv.setText(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelectionListener.onDateSelected(mYear, holder.date_tv.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView date_tv;

        public DateViewHolder(View itemView) {
            super(itemView);
            date_tv = (TextView) itemView.findViewById(R.id.date_tv);
        }
    }
}
