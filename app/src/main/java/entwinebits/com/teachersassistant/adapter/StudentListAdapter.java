package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.AddNewStudentActivity;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    private Activity mActivity;
    private ArrayList<UserProfileDTO> mStudentList;

    public StudentListAdapter(Activity activity, ArrayList<UserProfileDTO> list) {
        this.mActivity = activity;
        this.mStudentList = list;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_single_item, null);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {

        final UserProfileDTO dto = mStudentList.get(position);
        holder.student_name_tv.setText(dto.getUserName());
        holder.student_contact_tv.setText(dto.getUserMobilePhone());
        holder.student_monthly_fee.setText(dto.getMonthlyFee() + "");
        holder.student_list_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AddNewStudentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(Constants.EDIT_STUDENT_DTO, dto);
                mActivity.startActivity(intent);
            }
        });
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
            student_list_settings = (FrameLayout) itemView.findViewById(R.id.student_list_settings);
            student_name_tv = (TextView) itemView.findViewById(R.id.student_name_tv);
            student_contact_tv = (TextView) itemView.findViewById(R.id.student_contact_tv);
            student_monthly_fee = (TextView) itemView.findViewById(R.id.student_monthly_fee);
        }
    }
}
