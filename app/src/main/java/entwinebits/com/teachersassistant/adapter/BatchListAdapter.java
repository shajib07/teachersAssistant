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
        holder.batch_name_tv.setText(batchDTO.getBatchName());

        ArrayList<ScheduleDTO> scheduleDTOs = batchDTO.getScheduleDTOList();
        if (scheduleDTOs != null && scheduleDTOs.size() > 0) {
            holder.batch_schedule_from_tv.setText(scheduleDTOs.get(0).getStartTime());
            holder.batch_schedule_to_tv.setText(scheduleDTOs.get(0).getEndTime());
        }

        for (int dayIndex = 0; dayIndex < scheduleDTOs.size(); dayIndex++) {

            final ScheduleDTO dto = scheduleDTOs.get(dayIndex);
            holder.batch_schedule_from_tv.setText(dto.getStartTime());
            holder.batch_schedule_to_tv.setText(dto.getEndTime());

            switch (dto.getDaysOfWeek()) {
                case 0:
                    holder.day_week_ll_1.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_1.getChildAt(0)).setText("SAT");
                    holder.day_week_ll_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.batch_schedule_from_tv.setText(dto.getStartTime());
                            holder.batch_schedule_to_tv.setText(dto.getEndTime());

                            holder.dayWeekIndi[0].setVisibility(View.VISIBLE);
                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);

//                            holder.day_week_ll_1.getChildAt(1).setVisibility(View.VISIBLE);
//                            holder.day_week_ll_2.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_3.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_4.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_5.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_6.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_7.getChildAt(1).setVisibility(View.GONE);
                        }
                    });
                    break;
                case 1:
                    holder.day_week_ll_2.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_2.getChildAt(0)).setText("SUN");
                    holder.day_week_ll_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.batch_schedule_from_tv.setText(dto.getStartTime());
                            holder.batch_schedule_to_tv.setText(dto.getEndTime());

                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[1].setVisibility(View.VISIBLE);
                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);

//                            holder.day_week_ll_1.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_2.getChildAt(1).setVisibility(View.VISIBLE);
//                            holder.day_week_ll_3.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_4.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_5.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_6.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_7.getChildAt(1).setVisibility(View.GONE);
                        }
                    });
                    break;
                case 2:
                    holder.day_week_ll_3.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_3.getChildAt(0)).setText("MON");
                    holder.day_week_ll_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.batch_schedule_from_tv.setText(dto.getStartTime());
                            holder.batch_schedule_to_tv.setText(dto.getEndTime());

                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[2].setVisibility(View.VISIBLE);
                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);

//                            holder.day_week_ll_1.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_2.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_3.getChildAt(1).setVisibility(View.VISIBLE);
//                            holder.day_week_ll_4.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_5.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_6.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_7.getChildAt(1).setVisibility(View.GONE);
                        }
                    });
                    break;
                case 3:
                    holder.day_week_ll_4.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_4.getChildAt(0)).setText("TUE");
                    holder.day_week_ll_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.batch_schedule_from_tv.setText(dto.getStartTime());
                            holder.batch_schedule_to_tv.setText(dto.getEndTime());

                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[3].setVisibility(View.VISIBLE);
                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);


//                            holder.day_week_ll_1.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_2.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_3.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_4.getChildAt(1).setVisibility(View.VISIBLE);
//                            holder.day_week_ll_5.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_6.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_7.getChildAt(1).setVisibility(View.GONE);
                        }
                    });
                    break;
                case 4:
                    holder.day_week_ll_5.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_5.getChildAt(0)).setText("WED");
                    holder.day_week_ll_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.batch_schedule_from_tv.setText(dto.getStartTime());
                            holder.batch_schedule_to_tv.setText(dto.getEndTime());

                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[4].setVisibility(View.VISIBLE);
                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);


//                            holder.day_week_ll_1.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_2.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_3.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_4.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_5.getChildAt(1).setVisibility(View.VISIBLE);
//                            holder.day_week_ll_6.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_7.getChildAt(1).setVisibility(View.GONE);
                        }
                    });
                    break;
                case 5:
                    holder.day_week_ll_6.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_6.getChildAt(0)).setText("THU");
                    holder.day_week_ll_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.batch_schedule_from_tv.setText(dto.getStartTime());
                            holder.batch_schedule_to_tv.setText(dto.getEndTime());
                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[5].setVisibility(View.VISIBLE);
                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);

//                            holder.day_week_ll_1.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_2.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_3.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_4.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_5.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_6.getChildAt(1).setVisibility(View.VISIBLE);
//                            holder.day_week_ll_7.getChildAt(1).setVisibility(View.GONE);
                        }
                    });
                    break;
                case 6:
                    holder.day_week_ll_7.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_7.getChildAt(0)).setText("FRI");
                    holder.day_week_ll_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.batch_schedule_from_tv.setText(dto.getStartTime());
                            holder.batch_schedule_to_tv.setText(dto.getEndTime());

                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
                            holder.dayWeekIndi[6].setVisibility(View.VISIBLE);


//                            holder.day_week_ll_1.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_2.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_3.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_4.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_5.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_6.getChildAt(1).setVisibility(View.GONE);
//                            holder.day_week_ll_7.getChildAt(1).setVisibility(View.VISIBLE);
                        }
                    });
                    break;
                default:
                    break;
            }
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
        private LinearLayout day_week_ll_1, day_week_ll_2, day_week_ll_3, day_week_ll_4, day_week_ll_5, day_week_ll_6, day_week_ll_7;

        private View dayWeekIndi[] = new View[7];

        public BatchViewHolder(View itemView) {
            super(itemView);
            batch_name_tv = (TextView) itemView.findViewById(R.id.batch_name_tv);
            batch_schedule_from_tv = (TextView) itemView.findViewById(R.id.batch_schedule_from_tv);
            batch_schedule_to_tv = (TextView) itemView.findViewById(R.id.batch_schedule_to_tv);

            day_week_ll_1 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_1);
            day_week_ll_2 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_2);
            day_week_ll_3 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_3);
            day_week_ll_4 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_4);
            day_week_ll_5 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_5);
            day_week_ll_6 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_6);
            day_week_ll_7 = (LinearLayout) itemView.findViewById(R.id.day_week_ll_7);

            day_week_ll_1.setVisibility(View.GONE);
            day_week_ll_2.setVisibility(View.GONE);
            day_week_ll_3.setVisibility(View.GONE);
            day_week_ll_4.setVisibility(View.GONE);
            day_week_ll_5.setVisibility(View.GONE);
            day_week_ll_6.setVisibility(View.GONE);
            day_week_ll_7.setVisibility(View.GONE);

            dayWeekIndi[0] = itemView.findViewById(R.id.day_week_indi_1);
            dayWeekIndi[1] = itemView.findViewById(R.id.day_week_indi_2);
            dayWeekIndi[2] = itemView.findViewById(R.id.day_week_indi_3);
            dayWeekIndi[3] = itemView.findViewById(R.id.day_week_indi_4);
            dayWeekIndi[4] = itemView.findViewById(R.id.day_week_indi_5);
            dayWeekIndi[5] = itemView.findViewById(R.id.day_week_indi_6);
            dayWeekIndi[6] = itemView.findViewById(R.id.day_week_indi_7);

            dayWeekIndi[0].setVisibility(View.INVISIBLE);
            dayWeekIndi[1].setVisibility(View.INVISIBLE);
            dayWeekIndi[2].setVisibility(View.INVISIBLE);
            dayWeekIndi[3].setVisibility(View.INVISIBLE);
            dayWeekIndi[4].setVisibility(View.INVISIBLE);
            dayWeekIndi[5].setVisibility(View.INVISIBLE);
            dayWeekIndi[6].setVisibility(View.INVISIBLE);

        }
    }
}