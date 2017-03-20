package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
        BatchDTO batchDTO = mBatchDTOList.get(position);
        holder.batch_name_tv.setText(batchDTO.getBatchName());

        ArrayList<ScheduleDTO> scheduleDTOs = batchDTO.getScheduleDTOList();
        HelperMethod.debugLog(TAG, "scheduleDTOs size == "+scheduleDTOs.size());
        if (scheduleDTOs != null && scheduleDTOs.size() > 0) {

            String startTime = ConstantFunctions.getDate(scheduleDTOs.get(0).getStartTime(), Constants.TIME_12_HOUR_FORMAT);
            String endTime = ConstantFunctions.getDate(scheduleDTOs.get(0).getEndTime(), Constants.TIME_12_HOUR_FORMAT);

            holder.batch_schedule_from_tv.setText(startTime);
            holder.batch_schedule_to_tv.setText(endTime);
        }
        String tempDays = "";

        for (int dayIndex = 0; dayIndex < scheduleDTOs.size(); dayIndex++) {

            final ScheduleDTO dto = scheduleDTOs.get(dayIndex);
            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

            holder.batch_schedule_from_tv.setText(startTime);
            holder.batch_schedule_to_tv.setText(endTime);

            switch (dto.getDaysOfWeek()) {
                case 0:
                    tempDays += "SAT,";
                    holder.day_week_ll_1.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_1.getChildAt(0)).setText("SAT");
                    holder.day_week_ll_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                            holder.batch_schedule_from_tv.setText(startTime);
                            holder.batch_schedule_to_tv.setText(endTime);

//                            holder.dayWeekIndi[0].setVisibility(View.VISIBLE);
//                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);0
//                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);

                        }
                    });
                    break;
                case 1:
                    tempDays += "SUN,";
                    holder.day_week_ll_2.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_2.getChildAt(0)).setText("SUN");
                    holder.day_week_ll_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                            holder.batch_schedule_from_tv.setText(startTime);
                            holder.batch_schedule_to_tv.setText(endTime);

//                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[1].setVisibility(View.VISIBLE);
//                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);

                        }
                    });
                    break;
                case 2:
                    tempDays += "MON,";
                    holder.day_week_ll_3.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_3.getChildAt(0)).setText("MON");
                    holder.day_week_ll_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                            holder.batch_schedule_from_tv.setText(startTime);
                            holder.batch_schedule_to_tv.setText(endTime);

//                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[2].setVisibility(View.VISIBLE);
//                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);

                        }
                    });
                    break;
                case 3:
                    tempDays += "TUE,";
                    holder.day_week_ll_4.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_4.getChildAt(0)).setText("TUE");
                    holder.day_week_ll_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                            holder.batch_schedule_from_tv.setText(startTime);
                            holder.batch_schedule_to_tv.setText(endTime);

//                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[3].setVisibility(View.VISIBLE);
//                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);

                        }
                    });
                    break;
                case 4:
                    tempDays += "WED,";
                    holder.day_week_ll_5.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_5.getChildAt(0)).setText("WED");
                    holder.day_week_ll_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                            holder.batch_schedule_from_tv.setText(startTime);
                            holder.batch_schedule_to_tv.setText(endTime);

//                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[4].setVisibility(View.VISIBLE);
//                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);
//

                        }
                    });
                    break;
                case 5:
                    tempDays += "THU,";
                    holder.day_week_ll_6.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_6.getChildAt(0)).setText("THU");
                    holder.day_week_ll_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                            holder.batch_schedule_from_tv.setText(startTime);
                            holder.batch_schedule_to_tv.setText(endTime);
//                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[5].setVisibility(View.VISIBLE);
//                            holder.dayWeekIndi[6].setVisibility(View.INVISIBLE);
                        }
                    });
                    break;
                case 6:
                    tempDays += "FRI,";
                    holder.day_week_ll_7.setVisibility(View.VISIBLE);
                    ((TextView) holder.day_week_ll_7.getChildAt(0)).setText("FRI");
                    holder.day_week_ll_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String startTime = ConstantFunctions.getDate(dto.getStartTime(), Constants.TIME_12_HOUR_FORMAT);
                            String endTime = ConstantFunctions.getDate(dto.getEndTime(), Constants.TIME_12_HOUR_FORMAT);

                            holder.batch_schedule_from_tv.setText(startTime);
                            holder.batch_schedule_to_tv.setText(endTime);

//                            holder.dayWeekIndi[0].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[1].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[2].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[3].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[4].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[5].setVisibility(View.INVISIBLE);
//                            holder.dayWeekIndi[6].setVisibility(View.VISIBLE);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
        final String finalTempDays = tempDays;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BatchDTO dto = mBatchDTOList.get(position);
                Intent batchIntent = new Intent(mActivity, BatchDetailsActivity.class);
                batchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                batchIntent.putExtra(Constants.BATCH_ID, dto.getBatchId());
                batchIntent.putExtra(Constants.BATCH_NAME, dto.getBatchName());
                batchIntent.putExtra(Constants.BATCH_WEEK_DAYS, finalTempDays);
                batchIntent.putParcelableArrayListExtra(Constants.BATCH_SCHEDULE_LIST, dto.getScheduleDTOList());
                mActivity.startActivity(batchIntent);
            }
        });
//
//        holder.batch_list_settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final BatchDTO dto = mBatchDTOList.get(position);
//                String upperText = "Edit", lowerText = "Delete";
//                View.OnClickListener upperListener, lowerListener;
//                upperListener = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mActivity, AddNewBatchActivity.class);
//                        intent.putExtra(Constants.BATCH_ID, dto.getBatchId());
//                        intent.putExtra(Constants.BATCH_NAME, dto.getBatchName());
//                        intent.putExtra(Constants.BATCH_SCHEDULE_LIST, dto.getScheduleDTOList());
//                        mActivity.startActivity(intent);
//                    }
//                };
//                lowerListener = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {}
//                };
//                DialogProvider.showDoubleOptionDialog(mActivity, upperText, lowerText, upperListener, lowerListener);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (mBatchDTOList == null ? 0 : mBatchDTOList.size());
    }

    public class BatchViewHolder extends RecyclerView.ViewHolder {
        private TextView batch_name_tv, batch_schedule_from_tv, batch_schedule_to_tv;
        private LinearLayout day_week_ll_1, day_week_ll_2, day_week_ll_3, day_week_ll_4, day_week_ll_5, day_week_ll_6, day_week_ll_7;
        private FrameLayout batch_list_settings;
        private View dayWeekIndi[] = new View[7];

        public BatchViewHolder(View itemView) {
            super(itemView);
//            batch_list_settings = (FrameLayout) itemView.findViewById(R.id.batch_list_settings);
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

//            dayWeekIndi[0] = itemView.findViewById(R.id.day_week_indi_1);
//            dayWeekIndi[1] = itemView.findViewById(R.id.day_week_indi_2);
//            dayWeekIndi[2] = itemView.findViewById(R.id.day_week_indi_3);
//            dayWeekIndi[3] = itemView.findViewById(R.id.day_week_indi_4);
//            dayWeekIndi[4] = itemView.findViewById(R.id.day_week_indi_5);
//            dayWeekIndi[5] = itemView.findViewById(R.id.day_week_indi_6);
//            dayWeekIndi[6] = itemView.findViewById(R.id.day_week_indi_7);
//
//            dayWeekIndi[0].setVisibility(View.INVISIBLE);
//            dayWeekIndi[1].setVisibility(View.INVISIBLE);
//            dayWeekIndi[2].setVisibility(View.INVISIBLE);
//            dayWeekIndi[3].setVisibility(View.INVISIBLE);
//            dayWeekIndi[4].setVisibility(View.INVISIBLE);
//            dayWeekIndi[5].setVisibility(View.INVISIBLE);
//            dayWeekIndi[6].setVisibility(View.INVISIBLE);

        }
    }
}