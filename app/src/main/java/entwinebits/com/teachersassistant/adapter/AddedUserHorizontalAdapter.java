package entwinebits.com.teachersassistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.UserProfileDTO;

/**
 * Created by Nargis Rahman on 12/2/2016.
 */
public class AddedUserHorizontalAdapter extends RecyclerView.Adapter<AddedUserHorizontalAdapter.AddedUserViewHolder> {

    private static final String TAG = "AddUserAdapter";
    ArrayList<UserProfileDTO> mUserProfileDTOs = new ArrayList<>();

    public AddedUserHorizontalAdapter(ArrayList<UserProfileDTO> userProfileDTOs){
        this.mUserProfileDTOs = userProfileDTOs;
    }

    @Override
    public AddedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_user_single_item, parent, false);
        return new AddedUserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AddedUserViewHolder holder, final int position) {
        UserProfileDTO dto = mUserProfileDTOs.get(position);
        holder.user_name_tv.setText(dto.getUserName());

        holder.remove_user_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserProfileDTOs.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mUserProfileDTOs != null ? mUserProfileDTOs.size() : 0);
    }

    public class AddedUserViewHolder extends RecyclerView.ViewHolder {
        private TextView user_name_tv;
        private ImageView remove_user_iv;
        public AddedUserViewHolder(View itemView) {
            super(itemView);
            user_name_tv = (TextView) itemView.findViewById(R.id.user_name_tv);
            remove_user_iv = (ImageView)itemView.findViewById(R.id.remove_user_iv);
        }
    }
}
