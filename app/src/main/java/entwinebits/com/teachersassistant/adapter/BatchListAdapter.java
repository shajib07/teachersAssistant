package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entwinebits.com.teachersassistant.AddNewBatchActivity;
import entwinebits.com.teachersassistant.BatchActivity;
import entwinebits.com.teachersassistant.BatchDetailsActivity;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.ScheduleDTO;
import entwinebits.com.teachersassistant.utils.ConstantFunctions;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.DialogProvider;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class BatchListAdapter extends RecyclerView.Adapter<BatchListAdapter.BatchViewHolder> {

    private String TAG = "BatchListAdapter";
    private Activity mActivity;
    private ArrayList<BatchDTO> mBatchDTOList;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_list_single_item, parent, false);
        return new BatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BatchViewHolder holder, final int position) {
        final BatchDTO batchDTO = mBatchDTOList.get(position);
        holder.batch_name_tv.setText(batchDTO.getBatchName());

        ArrayList<ScheduleDTO> scheduleDTOs = batchDTO.getScheduleDTOList();
        HelperMethod.debugLog(TAG, "scheduleDTOs size == " + scheduleDTOs.size());

        holder.day_week_ll_1.setVisibility(View.GONE);
        holder.day_week_ll_2.setVisibility(View.GONE);
        holder.day_week_ll_3.setVisibility(View.GONE);
        holder.day_week_ll_4.setVisibility(View.GONE);
        holder.day_week_ll_5.setVisibility(View.GONE);
        holder.day_week_ll_6.setVisibility(View.GONE);
        holder.day_week_ll_7.setVisibility(View.GONE);

        holder.day_vert_line_1.setVisibility(View.GONE);
        holder.day_vert_line_2.setVisibility(View.GONE);
        holder.day_vert_line_3.setVisibility(View.GONE);
        holder.day_vert_line_4.setVisibility(View.GONE);
        holder.day_vert_line_5.setVisibility(View.GONE);
        holder.day_vert_line_6.setVisibility(View.GONE);
        holder.day_vert_line_7.setVisibility(View.GONE);

        holder.day_week_vert_ll_1.setVisibility(View.GONE);
        holder.day_week_vert_ll_2.setVisibility(View.GONE);
        holder.day_week_vert_ll_3.setVisibility(View.GONE);
        holder.day_week_vert_ll_4.setVisibility(View.GONE);
        holder.day_week_vert_ll_5.setVisibility(View.GONE);
        holder.day_week_vert_ll_6.setVisibility(View.GONE);
        holder.day_week_vert_ll_7.setVisibility(View.GONE);


        holder.schedule_more_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BatchDTO dto : mBatchDTOList) {
                    if (!dto.equals(batchDTO)) {
                        dto.setExpanded(false);
                    }
                }
                batchDTO.setExpanded(!batchDTO.isExpanded());
                notifyDataSetChanged();
            }
        });

        HelperMethod.debugLog(TAG, "batchDTO.isExpanded  "+batchDTO.isExpanded() );

        if (batchDTO.isExpanded()) {
            holder.schedule_less_iv.setVisibility(View.VISIBLE);
            holder.schedule_more_iv.setVisibility(View.GONE);

            holder.batch_schedule_horizontal_ll.setVisibility(View.GONE);
            holder.batch_schedule_vertical_ll.setVisibility(View.VISIBLE);
        } else {
            holder.schedule_less_iv.setVisibility(View.GONE);
            holder.schedule_more_iv.setVisibility(View.VISIBLE);

            holder.batch_schedule_horizontal_ll.setVisibility(View.VISIBLE);
            holder.batch_schedule_vertical_ll.setVisibility(View.GONE);
        }

        for (int dayIndex = 0; dayIndex < scheduleDTOs.size(); dayIndex++) {

            final ScheduleDTO dto = scheduleDTOs.get(dayIndex);
            String startTime, endTime;

            switch (dto.getDaysOfWeek()) {
                case 0:
                    holder.day_week_vert_ll_1.setVisibility(View.VISIBLE);
                    holder.day_vert_line_1.setVisibility(View.VISIBLE);

                    ((TextView) holder.day_week_vert_ll_1.getChildAt(0)).setText("SAT");
                    startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                    endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                    ((TextView) holder.day_week_vert_ll_1.getChildAt(1)).setText(startTime);
                    ((TextView) holder.day_week_vert_ll_1.getChildAt(2)).setText(endTime);

                    // handle non-expanded view
                    holder.day_week_ll_1.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_1.getChildAt(0)).setText("SAT");
                    break;

                case 1:
                    holder.day_week_vert_ll_2.setVisibility(View.VISIBLE);
                    holder.day_vert_line_2.setVisibility(View.VISIBLE);

                    ((TextView) holder.day_week_vert_ll_2.getChildAt(0)).setText("SUN");
                    startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                    endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                    ((TextView) holder.day_week_vert_ll_2.getChildAt(1)).setText(startTime);
                    ((TextView) holder.day_week_vert_ll_2.getChildAt(2)).setText(endTime);

                    // handle non-expanded view
                    holder.day_week_ll_2.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_2.getChildAt(0)).setText("SUN");
                    break;

                case 2:
                    holder.day_week_vert_ll_3.setVisibility(View.VISIBLE);
                    holder.day_vert_line_3.setVisibility(View.VISIBLE);

                    ((TextView) holder.day_week_vert_ll_3.getChildAt(0)).setText("MON");
                    startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                    endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                    ((TextView) holder.day_week_vert_ll_3.getChildAt(1)).setText(startTime);
                    ((TextView) holder.day_week_vert_ll_3.getChildAt(2)).setText(endTime);

                    // handle non-expanded view
                    holder.day_week_ll_3.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_3.getChildAt(0)).setText("MON");
                    break;

                case 3:
                    holder.day_week_vert_ll_4.setVisibility(View.VISIBLE);
                    holder.day_vert_line_4.setVisibility(View.VISIBLE);

                    ((TextView) holder.day_week_vert_ll_4.getChildAt(0)).setText("TUE");
                    startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                    endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                    ((TextView) holder.day_week_vert_ll_4.getChildAt(1)).setText(startTime);
                    ((TextView) holder.day_week_vert_ll_4.getChildAt(2)).setText(endTime);

                    // handle non-expanded view
                    holder.day_week_ll_4.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_4.getChildAt(0)).setText("TUE");
                    break;

                case 4:
                    holder.day_week_vert_ll_5.setVisibility(View.VISIBLE);
                    holder.day_vert_line_5.setVisibility(View.VISIBLE);

                    ((TextView) holder.day_week_vert_ll_5.getChildAt(0)).setText("WED");
                    startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                    endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                    ((TextView) holder.day_week_vert_ll_5.getChildAt(1)).setText(startTime);
                    ((TextView) holder.day_week_vert_ll_5.getChildAt(2)).setText(endTime);

                    // handle non-expanded view
                    holder.day_week_ll_5.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_5.getChildAt(0)).setText("WED");
                    break;

                case 5:
                    holder.day_week_vert_ll_6.setVisibility(View.VISIBLE);
                    holder.day_vert_line_6.setVisibility(View.VISIBLE);

                    ((TextView) holder.day_week_vert_ll_6.getChildAt(0)).setText("THU");
                    startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                    endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                    ((TextView) holder.day_week_vert_ll_6.getChildAt(1)).setText(startTime);
                    ((TextView) holder.day_week_vert_ll_6.getChildAt(2)).setText(endTime);

                    // handle non-expanded view
                    holder.day_week_ll_6.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_6.getChildAt(0)).setText("THU");
                    break;

                case 6:
                    holder.day_week_vert_ll_7.setVisibility(View.VISIBLE);
                    holder.day_vert_line_7.setVisibility(View.VISIBLE);

                    ((TextView) holder.day_week_vert_ll_7.getChildAt(0)).setText("FRI");
                    startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                    endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                    ((TextView) holder.day_week_vert_ll_7.getChildAt(1)).setText(startTime);
                    ((TextView) holder.day_week_vert_ll_7.getChildAt(2)).setText(endTime);

                    // handle non-expanded view
                    holder.day_week_ll_7.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_7.getChildAt(0)).setText("FRI");
                    break;

                default:
                    break;
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BatchDTO dto = mBatchDTOList.get(position);
                Intent batchIntent = new Intent(mActivity, BatchDetailsActivity.class);
                batchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                batchIntent.putExtra(Constants.BATCH_ID, dto.getBatchId());
                batchIntent.putExtra(Constants.BATCH_NAME, dto.getBatchName());
                batchIntent.putParcelableArrayListExtra(Constants.BATCH_SCHEDULE_LIST, dto.getScheduleDTOList());
                mActivity.startActivity(batchIntent);
            }
        });
}

    @Override
    public int getItemCount() {
        return (mBatchDTOList == null ? 0 : mBatchDTOList.size());
    }

    public class BatchViewHolder extends RecyclerView.ViewHolder {
        private TextView batch_name_tv, batch_schedule_from_tv, batch_schedule_to_tv;
        private LinearLayout day_week_ll_1, day_week_ll_2, day_week_ll_3, day_week_ll_4, day_week_ll_5, day_week_ll_6, day_week_ll_7;
        private View day_vert_line_1, day_vert_line_2, day_vert_line_3, day_vert_line_4, day_vert_line_5, day_vert_line_6, day_vert_line_7;
        private LinearLayout day_week_vert_ll_1, day_week_vert_ll_2, day_week_vert_ll_3, day_week_vert_ll_4, day_week_vert_ll_5,
                day_week_vert_ll_6, day_week_vert_ll_7;
        private FrameLayout schedule_more_fl;
        private ImageView schedule_more_iv, schedule_less_iv;
        private View dayWeekIndi[] = new View[7];
        private LinearLayout batch_schedule_vertical_ll, batch_schedule_horizontal_ll;

        public BatchViewHolder(View itemView) {
            super(itemView);
//            batch_list_settings = (FrameLayout) itemView.findViewById(R.id.batch_list_settings);
            batch_name_tv = (TextView) itemView.findViewById(R.id.batch_name_tv);
//            batch_schedule_from_tv = (TextView) itemView.findViewById(R.id.batch_schedule_from_tv);
//            batch_schedule_to_tv = (TextView) itemView.findViewById(R.id.batch_schedule_to_tv);
            schedule_more_fl = (FrameLayout) itemView.findViewById(R.id.schedule_more_fl);
            schedule_more_iv = (ImageView) itemView.findViewById(R.id.schedule_more_iv);
            schedule_less_iv = (ImageView) itemView.findViewById(R.id.schedule_less_iv);
            batch_schedule_vertical_ll = (LinearLayout) itemView.findViewById(R.id.batch_schedule_vertical_ll);
            batch_schedule_horizontal_ll = (LinearLayout) itemView.findViewById(R.id.batch_schedule_horizontal_ll);

            day_week_ll_1 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_1);
            day_week_ll_2 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_2);
            day_week_ll_3 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_3);
            day_week_ll_4 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_4);
            day_week_ll_5 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_5);
            day_week_ll_6 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_6);
            day_week_ll_7 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_7);

            day_week_vert_ll_1 = (LinearLayout) itemView.findViewById(R.id.day_week_vert_ll_1);
            day_week_vert_ll_2 = (LinearLayout) itemView.findViewById(R.id.day_week_vert_ll_2);
            day_week_vert_ll_3 = (LinearLayout) itemView.findViewById(R.id.day_week_vert_ll_3);
            day_week_vert_ll_4 = (LinearLayout) itemView.findViewById(R.id.day_week_vert_ll_4);
            day_week_vert_ll_5 = (LinearLayout) itemView.findViewById(R.id.day_week_vert_ll_5);
            day_week_vert_ll_6 = (LinearLayout) itemView.findViewById(R.id.day_week_vert_ll_6);
            day_week_vert_ll_7 = (LinearLayout) itemView.findViewById(R.id.day_week_vert_ll_7);

            day_vert_line_1 = itemView.findViewById(R.id.day_vert_line_1);
            day_vert_line_2 = itemView.findViewById(R.id.day_vert_line_2);
            day_vert_line_3 = itemView.findViewById(R.id.day_vert_line_3);
            day_vert_line_4 = itemView.findViewById(R.id.day_vert_line_4);
            day_vert_line_5 = itemView.findViewById(R.id.day_vert_line_5);
            day_vert_line_6 = itemView.findViewById(R.id.day_vert_line_6);
            day_vert_line_7 = itemView.findViewById(R.id.day_vert_line_7);

            day_week_ll_1.setVisibility(View.GONE);
            day_week_ll_2.setVisibility(View.GONE);
            day_week_ll_3.setVisibility(View.GONE);
            day_week_ll_4.setVisibility(View.GONE);
            day_week_ll_5.setVisibility(View.GONE);
            day_week_ll_6.setVisibility(View.GONE);
            day_week_ll_7.setVisibility(View.GONE);
        }
    }
}