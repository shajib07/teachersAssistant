package entwinebits.com.teachersassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by shajib on 1/24/2017.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout search_toolbar_back;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);


        initLayout();
    }

    private void initLayout() {
        search_toolbar_back = (FrameLayout)findViewById(R.id.search_toolbar_back);
        search_toolbar_back.setOnClickListener(this);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Name/Mobile Phone");

        final EditText searchET = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        if (searchET != null) {
            searchET.setTextColor(getResources().getColor(android.R.color.black));
            searchET.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        }

        ImageView searchIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        if (searchIcon != null) {
            searchIcon.setImageResource(R.mipmap.ic_action_add);
            searchIcon.setAlpha(0.6f);
        }

        searchView.onActionViewExpanded();

        ImageView crossButton = (ImageView) searchET.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        if (crossButton != null) {
            crossButton.setImageResource(R.mipmap.ic_action_add);
            crossButton.setAlpha(0.5f);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.search_toolbar_back:
                SearchActivity.this.finish();
                break;
        }
    }
}
