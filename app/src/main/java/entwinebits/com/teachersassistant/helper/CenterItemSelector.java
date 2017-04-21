package entwinebits.com.teachersassistant.helper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.ItemSelectionAdapter;
import entwinebits.com.teachersassistant.interfaces.ItemSelectionType;
import entwinebits.com.teachersassistant.model.ItemSelectionDTO;

/**
 * Created by Nargis Rahman on 4/20/2017.
 */
public class CenterItemSelector {



    public float firstItemWidthDate;
    public float paddingDate;
    public float itemWidthDate;
    public int allPixelsDate;
    public int finalWidthDate;
    private ItemSelectionAdapter dateAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<ItemSelectionType> items;
    private String[] list;
    private int itemTypeId = 0;
    private ItemSelectionAdapter.ICenterItemListener iCenterItemListener;
    public CenterItemSelector(String[] list, RecyclerView recyclerView, Context context){
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = new ArrayList<>();
        this.list = list;
        this.iCenterItemListener = ( ItemSelectionAdapter.ICenterItemListener)context;
    }
    public void getRecyclerviewDate() {
        final RecyclerView recyclerViewDate = this.recyclerView;
        if (recyclerViewDate != null) {
            recyclerViewDate.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDateValue();
                }
            }, 300);
            recyclerViewDate.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerViewDate.smoothScrollToPosition(dateAdapter.getItemCount()-1);
                    setDateValue();
                }
            }, 5000);
        }



        ViewTreeObserver vtoDate = recyclerViewDate.getViewTreeObserver();
        vtoDate.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {


            @Override
            public boolean onPreDraw() {
                recyclerViewDate.getViewTreeObserver().removeOnPreDrawListener(this);
                finalWidthDate = recyclerViewDate.getMeasuredHeight();
                itemWidthDate = context.getResources().getDimension(R.dimen.item_dob_width);
                paddingDate = (finalWidthDate - itemWidthDate) / 2;
                firstItemWidthDate = paddingDate;
                allPixelsDate = 0;

                final LinearLayoutManager dateLayoutManager = new LinearLayoutManager(context);
                dateLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewDate.setLayoutManager(dateLayoutManager);
                recyclerViewDate.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        synchronized (this) {
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                calculatePositionAndScrollDate(recyclerView);
                            }
                        }

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        allPixelsDate += dy;
                    }
                });
                if (items == null) {
                    items = new ArrayList<>();
                }


                genLabelerDate(list);
                dateAdapter = new ItemSelectionAdapter(items, recyclerViewDate, iCenterItemListener, (int) firstItemWidthDate);
                recyclerViewDate.setAdapter(dateAdapter);
                dateAdapter.setSelecteditem(dateAdapter.getItemCount() - 1);

                return true;
            }
        });

    }


    private void genLabelerDate(String[] arr) {
        for (int i = 0; i <= arr.length+1; i++) {
            ItemSelectionDTO labelerDate = new ItemSelectionDTO();
            items.add(labelerDate);

            if (i == 0 || i == arr.length+1) {
                labelerDate.setItemName(String.valueOf(i));
                labelerDate.setItemType(ItemSelectionAdapter.VIEW_TYPE_PADDING);
            } else {
                labelerDate.setItemTypeId(itemTypeId);
                labelerDate.setItemType(ItemSelectionAdapter.VIEW_TYPE_ITEM);
                labelerDate.setItemName(arr[i-1]);
            }
        }
    }
/* this if most important, if expectedPositionDate < 0 recyclerView will return to nearest item*/

    private void calculatePositionAndScrollDate(RecyclerView recyclerView) {
        int expectedPositionDate = Math.round((allPixelsDate + paddingDate - firstItemWidthDate) / itemWidthDate);

        if (expectedPositionDate == -1) {
            expectedPositionDate = 0;
        } else if (expectedPositionDate >= recyclerView.getAdapter().getItemCount() - 2) {
            expectedPositionDate--;
        }
        scrollListToPositionDate(recyclerView, expectedPositionDate);

    }

    /* this if most important, if expectedPositionDate < 0 recyclerView will return to nearest item*/
    private void scrollListToPositionDate(RecyclerView recyclerView, int expectedPositionDate) {
        float targetScrollPosDate = expectedPositionDate * itemWidthDate + firstItemWidthDate - paddingDate;
        float missingPxDate = targetScrollPosDate - allPixelsDate;
        if (missingPxDate != 0) {
            recyclerView.smoothScrollBy(0, (int) missingPxDate);
        }
        setDateValue();
    }

    //
    private void setDateValue() {
        int expectedPositionDateColor = Math.round((allPixelsDate + paddingDate - firstItemWidthDate) / itemWidthDate);
        int setColorDate = expectedPositionDateColor + 1;
//        set color here
        dateAdapter.setSelecteditem(setColorDate);
    }
}
