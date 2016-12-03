package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import entwinebits.com.teachersassistant.R;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    private Activity mActivity;

    public StudentListAdapter(Activity activity) {
        this.mActivity = mActivity;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_single_item, null);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        public StudentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
