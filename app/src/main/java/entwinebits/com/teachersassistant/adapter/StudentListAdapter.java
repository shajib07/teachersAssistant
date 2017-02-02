package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.AddNewStudentActivity;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.StudentDetailsActivity;
import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.CustomDialog;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    private Activity mActivity;
    private ArrayList<UserProfileDTO> mStudentList;
    private long mBatchId;
    private DatabaseRequestHelper mDbRequestHelper;

    public StudentListAdapter(Activity activity, ArrayList<UserProfileDTO> list, long batchId) {
        this.mActivity = activity;
        this.mStudentList = list;
        this.mBatchId = batchId;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_single_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {

        final UserProfileDTO dto = mStudentList.get(position);
        holder.student_name_tv.setText(dto.getUserName());
        holder.student_contact_tv.setText(dto.getUserMobilePhone() == null ? mActivity.getString(R.string.not_set) : dto.getUserMobilePhone());
        holder.student_monthly_fee.setText(dto.getMonthlyFee() + "");
//        holder.student_list_settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showMenuDialog(dto);
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, StudentDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(Constants.EDIT_STUDENT_DTO, dto);
                intent.putExtra(Constants.BATCH_ID, (int)mBatchId);
                mActivity.startActivityForResult(intent, 120);
            }
        });
    }
//    <!--http://stackoverflow.com/questions/16694786/how-to-customize-a-spinner-in-android-->
    private void showMenuDialog(final UserProfileDTO dto) {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.student_menu_dialog_layout);
            dialog.setCancelable(true);

            final TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
            dialog_title.setText("Please Choose");

            final Button dialog_upper_btn = (Button) dialog.findViewById(R.id.dialog_upper_btn);
            dialog_upper_btn.setText("Edit");
            dialog_upper_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(mActivity, AddNewStudentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra(Constants.EDIT_STUDENT_DTO, dto);
                    mActivity.startActivityForResult(intent, 120);
                }
            });

            final Button dialog_lower_btn = (Button) dialog.findViewById(R.id.dialog_lower_btn);
            dialog_lower_btn.setText("Delete");
            dialog_lower_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(mActivity, StudentDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra(Constants.EDIT_STUDENT_DTO, dto);
                    mActivity.startActivity(intent);

//                    showDeleteConfirmationDialog(dto);
                }
            });
            dialog.show();

        } catch (Exception e) {
        }
    }

    private void showDeleteConfirmationDialog(final UserProfileDTO dto) {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.double_btn_dialog_layout);
            dialog.setCancelable(true);

            final Button dialog_left_btn = (Button) dialog.findViewById(R.id.dialog_left_btn);
            dialog_left_btn.setText("Cancel");
            dialog_left_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            final Button dialog_right_btn = (Button) dialog.findViewById(R.id.dialog_right_btn);
            dialog_right_btn.setText("OK");
            dialog_right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    mStudentList.remove(dto);
                    notifyDataSetChanged();
                    if (mDbRequestHelper == null) {
                        mDbRequestHelper = new DatabaseRequestHelper(mActivity);
                    }
                    mDbRequestHelper.deleteStudent(dto);
                }
            });
            dialog.show();

        } catch (Exception e) {
        }

    }

    @Override
    public int getItemCount() {
        return (mStudentList == null ? 0 : mStudentList.size());
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout student_list_settings;
        private TextView student_name_tv, student_contact_tv, student_monthly_fee;

        public StudentViewHolder(View itemView) {
            super(itemView);
//            student_list_settings = (FrameLayout) itemView.findViewById(R.id.student_list_settings);
            student_name_tv = (TextView) itemView.findViewById(R.id.student_name_tv);
            student_contact_tv = (TextView) itemView.findViewById(R.id.student_contact_tv);
            student_monthly_fee = (TextView) itemView.findViewById(R.id.student_monthly_fee);
        }
    }


}
