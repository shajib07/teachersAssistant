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

        for (final ScheduleDTO dto : scheduleDTOs) {
            dto.getDaysOfWeek();
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
            holder.weekDaysTv[mDayIndex].setVisibility(View.VISIBLE);
            holder.weekDaysTv[mDayIndex].setText(curDay);
            holder.weekDaysTv[mDayIndex].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.batch_schedule_tv.setText("Start "+dto.getStartTime()+ " End "+dto.getEndTime());
                }
            });

            mDayIndex++;
            HelperMethod.debugLog(TAG, "schedule day = "+dto.getDaysOfWeek()+" st = "+dto.getStartTime());

        }
        mDayIndex = 0;
        holder.batch_name_tv.setText(batchDTO.getBatchName());
//        holder.batch_schedule_tv.setText("11 AM");
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
        private TextView[] weekDaysTv = new TextView[7];
        public BatchViewHolder(View itemView) {
            super(itemView);
            batch_name_tv = (TextView)itemView.findViewById(R.id.batch_name_tv);
            batch_schedule_tv = (TextView)itemView.findViewById(R.id.batch_schedule_tv);

            weekDaysTv[0] = (TextView)itemView.findViewById(R.id.day_week_1);
            weekDaysTv[1] = (TextView)itemView.findViewById(R.id.day_week_2);
            weekDaysTv[2] = (TextView)itemView.findViewById(R.id.day_week_3);
            weekDaysTv[3] = (TextView)itemView.findViewById(R.id.day_week_4);
            weekDaysTv[4] = (TextView)itemView.findViewById(R.id.day_week_5);
            weekDaysTv[5] = (TextView)itemView.findViewById(R.id.day_week_6);
            weekDaysTv[6] = (TextView)itemView.findViewById(R.id.day_week_7);
        }
    }
}