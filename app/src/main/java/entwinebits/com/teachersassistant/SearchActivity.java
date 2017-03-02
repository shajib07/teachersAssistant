package entwinebits.com.teachersassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.SearchUserAdapter;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.server.ServerRequestHelper;
import entwinebits.com.teachersassistant.server.ServerResponseParser;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.ServerConstants;

/**
 * Created by shajib on 1/24/2017.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener, SearchUserAdapter.ItemSelectionListener {

    private String TAG = "SearchActivity";
    private FrameLayout search_toolbar_back;
    private SearchView searchView;

    private RecyclerView search_user_list_rv;
    private SearchUserAdapter mSearchUserAdapter;
    private ArrayList<UserProfileDTO> mSearchUserList ;
    private boolean isNewStudentSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        mSearchUserList = new ArrayList<>();

        if (getIntent().hasExtra(Constants.ADD_STUDENT_FROM_SEARCH)) {
            isNewStudentSearch = getIntent().getBooleanExtra(Constants.ADD_STUDENT_FROM_SEARCH, false);
        }

        initLayout();
    }

    private void initLayout() {
        search_toolbar_back = (FrameLayout)findViewById(R.id.search_toolbar_back);
        search_toolbar_back.setOnClickListener(this);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Name/Mobile Phone");

        initSearchView();

        search_user_list_rv = (RecyclerView)findViewById(R.id.search_user_list_rv);
        search_user_list_rv.setLayoutManager(new LinearLayoutManager(this));
        mSearchUserAdapter = new SearchUserAdapter(this, mSearchUserList, this, isNewStudentSearch);
        search_user_list_rv.setAdapter(mSearchUserAdapter);

    }

    private void initSearchView() {
        final EditText searchET = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        if (searchET != null) {
            searchET.setTextColor(getResources().getColor(android.R.color.black));
            searchET.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        }

        ImageView searchIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        if (searchIcon != null) {
            searchIcon.setImageResource(R.drawable.ic_action_add);
            searchIcon.setAlpha(0.6f);
        }

        searchView.onActionViewExpanded();

        ImageView crossButton = (ImageView) searchET.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        if (crossButton != null) {
            crossButton.setImageResource(R.drawable.ic_action_add);
            crossButton.setAlpha(0.5f);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                HelperMethod.debugLog(TAG, "onQueryTextSubmit : "+query);
                sendSearchJsonRequest(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
    private void sendSearchJsonRequest(String srchParam) {

        JSONObject jsonObject = ServerRequestHelper.sendUserSearchRequest(srchParam);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Constants.REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HelperMethod.debugLog(TAG, response.toString());
                        try {
                            if (!response.getBoolean(ServerConstants.ERROR)) {
                                mSearchUserList.clear();
                                mSearchUserList.addAll(ServerResponseParser.parseUserSearchResponse(response));
                                mSearchUserAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HelperMethod.debugLog(TAG, "Error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.search_toolbar_back:
                SearchActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemSelected(UserProfileDTO dto) {
        Intent BackIntent = new Intent();
//        HelperMethod.debugLog(AddNewBatchActivity.TAG, "mAddedStudentList : " + dto.size());
        BackIntent.putExtra(Constants.ADDED_STUDENT, dto);
        setResult(RESULT_OK, BackIntent);
        finish();
    }
}
