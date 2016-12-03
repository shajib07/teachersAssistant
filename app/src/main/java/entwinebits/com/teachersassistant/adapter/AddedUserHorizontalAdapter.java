package entwinebits.com.teachersassistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.UserProfileDTO;

/**
 * Created by Nargis Rahman on 12/2/2016.
 */
public class AddedUserHorizontalAdapter extends RecyclerView.Adapter<AddedUserHorizontalAdapter.AddedUserViewHolder> {

    private static final String TAG = "AddUserAdapter";
    ArrayList<UserProfileDTO> userProfileDTOs = new ArrayList<>();

    public AddedUserHorizontalAdapter(ArrayList<UserProfileDTO> userProfileDTOs){
        this.userProfileDTOs = userProfileDTOs;
    }

    @Override
    public AddedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_user_single_item, parent, false);
        return new AddedUserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AddedUserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AddedUserViewHolder extends RecyclerView.ViewHolder {
        public AddedUserViewHolder(View itemView) {
            super(itemView);
        }
    }
}
