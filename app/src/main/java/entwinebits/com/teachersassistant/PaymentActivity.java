package entwinebits.com.teachersassistant;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.adapter.ItemSelectionAdapter;
import entwinebits.com.teachersassistant.customview.BubbleLinearLayout;
import entwinebits.com.teachersassistant.helper.CenterItemSelector;
import entwinebits.com.teachersassistant.interfaces.ItemSelectionType;
import entwinebits.com.teachersassistant.model.ItemSelectionDTO;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, ItemSelectionAdapter.ICenterItemListener {

    private ArrayList<ItemSelectionType> labelerDates = new ArrayList<>();

     private final int  START_BUBBLE_LL_SELECTED = 1;
     private final int  END_BUBBLE_LL_SELECTED = 2;

    private CenterItemSelector centerItemSelectorMonth, centerItemSelectorYear;
    private TextView start_month_tv, start_year_tv, end_month_tv, end_year_tv;

    String arrMonth[] = {"Jan", "Feb", "Mar", "App", "May", "Jun", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec"};

    String arrYear[] = {"2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
            "2010", "2010", "2010", "2011", "2012", "2013","2014", "2015", "2016", "2017", "2018", "2019"};
    Button btn;
    LinearLayout linearLayout;
    private RelativeLayout root_rl;
    BubbleLinearLayout start_bubble_ll, end_bubble_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payemnt);

        initLayout();
        initBubbleView();
        //animateSelectionLayer();
        root_rl = (RelativeLayout)findViewById(R.id.root_rl);
        root_rl.setOnClickListener(this);

        start_bubble_ll.bubbleColor = getResources().getColor(R.color.colorPrimary);
        btn = (Button)findViewById(R.id.btn);
        linearLayout =(LinearLayout) findViewById(R.id.selection_linear);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateSelectionLayer();
            }
        });
        centerItemSelectorMonth = new CenterItemSelector(arrMonth, (RecyclerView)findViewById(R.id.monthSelector).findViewById(R.id.itemSelectionRecyclerView), this);
        centerItemSelectorMonth.getRecyclerviewDate();
        centerItemSelectorYear = new CenterItemSelector(arrYear, (RecyclerView)findViewById(R.id.yearSelector).findViewById(R.id.itemSelectionRecyclerView), this);
        centerItemSelectorYear.getRecyclerviewDate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //animateSelectionLayer();
    }

    private void initBubbleView(){

        start_bubble_ll = (BubbleLinearLayout)findViewById(R.id.start_bubble_ll);
        start_bubble_ll.setOnClickListener(this);
        end_bubble_ll = (BubbleLinearLayout)findViewById(R.id.end_bubble_ll);
        end_bubble_ll.setOnClickListener(this);
    }

    private void setSelectedBubbleView(){

    }

    private void setTextColor(TextView view, boolean isSelected){
        int color = isSelected?getResources().getColor(R.color.colorPrimaryDark):getResources().getColor(R.color.black);
        view.setTextColor(color);
    }

    private void setBubbleColor(BubbleLinearLayout view, boolean isSelected){
        int color = isSelected?getResources().getColor(R.color.blue_mist):getResources().getColor(R.color.colorPrimaryDark);
        view.bubbleColor = color;
    }

    private void initLayout(){
        start_month_tv = (TextView)findViewById(R.id.start_month_tv);
        start_year_tv = (TextView)findViewById(R.id.start_year_tv);
        end_month_tv = (TextView)findViewById(R.id.end_month_tv);
        end_year_tv = (TextView)findViewById(R.id.end_year_tv);
    }

    boolean b = false;
    private void animateSelectionLayer() {

        if(linearLayout.getVisibility() == View.GONE) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        if(!b){


            ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", linearLayout.getHeight(), 0);
            animator.setDuration(500);
            animator.start();

        }else {

            ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", 0, linearLayout.getHeight());
            animator.setDuration(500);
            animator.start();

        }
        b = !b;
    }

    @Override
    public void onCenterItemChanged(ItemSelectionType selectedItem, RecyclerView recyclerView) {

        if(selectedBubbleView == START_BUBBLE_LL_SELECTED) {
            if (recyclerView == (RecyclerView) findViewById(R.id.monthSelector).findViewById(R.id.itemSelectionRecyclerView)) {
                start_month_tv.setText(selectedItem.getItemName());
            } else if (recyclerView == (RecyclerView) findViewById(R.id.yearSelector).findViewById(R.id.itemSelectionRecyclerView)) {
                start_year_tv.setText(selectedItem.getItemName());
            }
        }else if(selectedBubbleView == END_BUBBLE_LL_SELECTED) {
            if (recyclerView == (RecyclerView) findViewById(R.id.monthSelector).findViewById(R.id.itemSelectionRecyclerView)) {
                end_month_tv.setText(selectedItem.getItemName());
            } else if (recyclerView == (RecyclerView) findViewById(R.id.yearSelector).findViewById(R.id.itemSelectionRecyclerView)) {
                end_year_tv.setText(selectedItem.getItemName());
            }
        } else {
            if (recyclerView == (RecyclerView) findViewById(R.id.monthSelector).findViewById(R.id.itemSelectionRecyclerView)) {
                start_month_tv.setText(selectedItem.getItemName());
                end_month_tv.setText(selectedItem.getItemName());
            } else if (recyclerView == (RecyclerView) findViewById(R.id.yearSelector).findViewById(R.id.itemSelectionRecyclerView)) {
                start_year_tv.setText(selectedItem.getItemName());
                end_year_tv.setText(selectedItem.getItemName());
            }

        }

    }

    int selectedBubbleView = 0;
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.start_bubble_ll:
                if(!b) {
                    animateSelectionLayer();
                }
                if(selectedBubbleView == END_BUBBLE_LL_SELECTED){
                    setTextColor(end_month_tv, false);
                    setTextColor(end_year_tv, false);
                    setBubbleColor(end_bubble_ll, false);

                }
                selectedBubbleView = START_BUBBLE_LL_SELECTED;
                setTextColor(start_month_tv, true);
                setTextColor(start_year_tv, true);
                setBubbleColor(start_bubble_ll, true);
                break;
            case R.id.end_bubble_ll:
                if(!b) {
                    animateSelectionLayer();
                }
                if(selectedBubbleView == START_BUBBLE_LL_SELECTED){
                    setTextColor(start_month_tv, false);
                    setTextColor(start_year_tv, false);
                    setBubbleColor(start_bubble_ll, false);

                }
                selectedBubbleView = END_BUBBLE_LL_SELECTED;
                setTextColor(end_month_tv, true);
                setTextColor(end_year_tv, true);
                setBubbleColor(end_bubble_ll, true);
                break;
            default:
                if(b) {
                    animateSelectionLayer();
                }
                setTextColor(start_month_tv, false);
                setTextColor(start_year_tv, false);
                setBubbleColor(start_bubble_ll, false);
                setTextColor(end_month_tv, false);
                setTextColor(end_year_tv, false);
                setBubbleColor(end_bubble_ll, false);
                selectedBubbleView = 0;
                break;

        }
    }
}
