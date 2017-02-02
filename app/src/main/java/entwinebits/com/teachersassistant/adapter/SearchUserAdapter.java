package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 2/1/2017.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserHolder> {

    private String TAG = "SearchUserAdapter";
    private ArrayList<UserProfileDTO> mSearchUserList;
    private Activity mActivity;

    public SearchUserAdapter(Activity activity, ArrayList<UserProfileDTO> list) {
        this.mActivity = activity;
        this.mSearchUserList = list;
    }

    @Override
    public SearchUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_single_item, parent, false);
        return new SearchUserHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchUserHolder holder, int position) {

        UserProfileDTO dto = mSearchUserList.get(position);
        holder.search_user_name_tv.setText(dto.getUserFirstName());
        holder.search_user_mobile_phn.setText((dto.getUserMobilePhone() == null || dto.getUserMobilePhone().length() == 0)
                ? mActivity.getString(R.string.not_set) : dto.getUserMobilePhone());
        holder.search_user_institute.setText((dto.getUserInstituteName() == null || dto.getUserInstituteName().length() == 0)
                ? mActivity.getString(R.string.not_set) : dto.getUserInstituteName());
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
