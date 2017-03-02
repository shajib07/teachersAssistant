package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.ProfileActivity;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 2/1/2017.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserHolder> {

    private String TAG = "SearchUserAdapter";
    private ArrayList<UserProfileDTO> mSearchUserList;
    private Activity mActivity;
    private ItemSelectionListener itemSelectionListener;
    private boolean isNewStudentSearch;

    public SearchUserAdapter(Activity activity, ArrayList<UserProfileDTO> list, ItemSelectionListener listener, boolean isNewStudentSearch) {
        this.mActivity = activity;
        this.mSearchUserList = list;
        this.itemSelectionListener = listener;
        this.isNewStudentSearch = isNewStudentSearch;
    }

    public interface ItemSelectionListener {
        void onItemSelected(UserProfileDTO dto);
    }

    @Override
    public SearchUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_single_item, parent, false);
        return new SearchUserHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchUserHolder holder, final int position) {

        HelperMethod.debugLog(TAG, "onBindViewHolder " +position);
        UserProfileDTO dto = mSearchUserList.get(position);
        holder.search_user_name_tv.setText(dto.getUserFirstName());
        holder.search_user_mobile_phn.setText((dto.getUserMobilePhone() == null || dto.getUserMobilePhone().length() == 0)
                ? mActivity.getString(R.string.not_set) : dto.getUserMobilePhone());
        holder.search_user_institute.setText((dto.getUserInstituteName() == null || dto.getUserInstituteName().length() == 0)
                ? mActivity.getString(R.string.not_set) : dto.getUserInstituteName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNewStudentSearch) {
                    itemSelectionListener.onItemSelected(mSearchUserList.get(position));
                    return;
                }
                Intent intent = new Intent(mActivity, ProfileActivity.class);
                intent.putExtra(Constants.USER_PROFILE_DTO, mSearchUserList.get(position));
                mActivity.startActivity(intent);
            }
        });
//        HelperMethod.debugLog(TAG, "tag2 == "+  mActivity.getString(R.string.not_set) + " " +dto.getUserMobilePhone() + " ins "+dto.getUserInstituteName());

    }

    @Override
    public int getItemCount() {
        return mSearchUserList == null ? 0 : mSearchUserList.size();
    }

    public class SearchUserHolder extends RecyclerView.ViewHolder {
        private TextView search_user_name_tv, search_user_mobile_phn, search_user_institute;
        public SearchUserHolder(View itemView) {
            super(itemView);

            search_user_name_tv = (TextView)itemView.findViewById(R.id.search_user_name_tv);
            search_user_mobile_phn = (TextView)itemView.findViewById(R.id.search_user_mobile_phn);
            search_user_institute = (TextView)itemView.findViewById(R.id.search_user_institute);
        }
    }
}
