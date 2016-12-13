package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class BatchListAdapter extends RecyclerView.Adapter<BatchListAdapter.BatchViewHolder> {

    private String TAG = "BatchListAdapter";
    private Activity mActivity;
    private ArrayList<BatchDTO> mBatchDTOList;
    private int mDayIndex = 0;

    public BatchListAdapter(Activity activity, ArrayList<BatchDTO> batchDTOList) {
        this.mActivity = activity;
        this.mBatchDTOList = batchDTOList;
    }

    public void notifyAdapterData(ArrayList<BatchDTO> list) {
        if (mBatchDTOList == null) {
            mBatchDTOList = new ArrayList<>();
        }
        mBatchDTOList = list;
        notifyDataSetChanged();
    }

    @Override
    public BatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_list_single_item, null);
        return new BatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BatchViewHolder holder, int position) {
        BatchDTO batchDTO = mBatchDTOList.get(position);
        ArrayList<ScheduleDTO> scheduleDTOs = batchDTO.getScheduleDTOList();

        mDayIndex = 0;
        for (final ScheduleDTO dto : scheduleDTOs) {

            String curDay = "";
            switch (dto.getDaysOfWeek()) {
                case 0:
                    curDay = "SAT";
                    break;
                case 1:
                    curDay = "SUN";
                    break;
                case 2:
                    curDay = "MON";
                    break;
                case 3:
                    curDay = "TUE";
                    break;
                case 4:
                    curDay = "WED";
                    break;
                case 5:
                    curDay = "THU";
                    break;
                case 6:
                    curDay = "FRI";
                    break;
                default:
                    break;
            }
            holder.batch_schedule_from_tv.setText(dto.getStartTime());
            holder.batch_schedule_to_tv.setText(dto.getEndTime());

            holder.weekDaysLL[mDayIndex].setVisibility(View.VISIBLE);
            TextView tv = (TextView) holder.weekDaysLL[mDayIndex].getChildAt(0);
            tv.setText(curDay);

            holder.weekDaysLL[mDayIndex].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.batch_schedule_from_tv.setText(dto.getStartTime());
                    holder.batch_schedule_to_tv.setText(dto.getEndTime());
                    View view = ((LinearLayout)v).getChildAt(1);
                    view.setVisibility(View.VISIBLE);
                }
            });
            mDayIndex++;
        }

        holder.batch_name_tv.setText(batchDTO.getBatchName());
        if (scheduleDTOs != null && scheduleDTOs.size() > 0) {
            holder.batch_schedule_from_tv.setText(scheduleDTOs.get(0).getStartTime());
            holder.batch_schedule_to_tv.setText(scheduleDTOs.get(0).getEndTime());
        }
    }

    @Override
    public int getItemCount() {
        if (mBatchDTOList != null) {
            return mBatchDTOList.size();
        }
        return 0;
    }

    public class BatchViewHolder extends RecyclerView.ViewHolder {
        private TextView batch_name_tv, batch_schedule_from_tv, batch_schedule_to_tv;
        private LinearLayout weekDaysLL[] = new LinearLayout[7];

        public BatchViewHolder(View itemView) {
            super(itemView);
            batch_name_tv = (TextView)itemView.findViewById(R.id.batch_name_tv);
            batch_schedule_from_tv = (TextView)itemView.findViewById(R.id.batch_schedule_from_tv);
            batch_schedule_to_tv = (TextView)itemView.findViewById(R.id.batch_schedule_to_tv);

            weekDaysLL[0] = (LinearLayout)itemView.findViewById(R.id.day_week_ll_1);
            weekDaysLL[1] = (LinearLayout)itemView.findViewById(R.id.day_week_ll_2);
            weekDaysLL[2] = (LinearLayout)itemView.findViewById(R.id.day_week_ll_3);
            weekDaysLL[3] = (LinearLayout)itemView.findViewById(R.id.day_week_ll_4);
            weekDaysLL[4] = (LinearLayout)itemView.findViewById(R.id.day_week_ll_5);
            weekDaysLL[5] = (LinearLayout)itemView.findViewById(R.id.day_week_ll_6);
            weekDaysLL[6] = (LinearLayout)itemView.findViewById(R.id.day_week_ll_7);

            for (int i = 0; i < 7; i++) {
                weekDaysLL[i].setVisibility(View.GONE);
            }

        }
    }
}