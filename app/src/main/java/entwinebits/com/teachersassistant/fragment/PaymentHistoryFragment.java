package entwinebits.com.teachersassistant.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import entwinebits.com.teachersassistant.listener.DateSelectionListener;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.BatchPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.adapter.DateChooserAdapter;
import entwinebits.com.teachersassistant.model.BatchDTO;

/**
 * Created by Nargis Rahman on 12/2/2016.
 */
public class PaymentHistoryFragment extends Fragment implements View.OnClickListener, DateSelectionListener {

    private Activity mActivity;
    private View mView;
    private RecyclerView payment_history_rv;
    private ArrayList<BatchDTO> mBatchList;

    private LinearLayout from_ll, to_ll;
    private TextView from_month_tv, to_month_tv, from_year_tv, to_year_tv;
    private Calendar mCalendar;
    private FrameLayout search_layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mActivity == null) {
            mActivity = getActivity();
        }
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_payment_history_layout, container, false);
            initData();
            initLayout();
        }
        mCalendar = Calendar.getInstance();
        return mView;
    }

    private void initLayout() {
        BatchPaymentHistoryAdapter batchHistoryAdapter = new BatchPaymentHistoryAdapter(mActivity);
        payment_history_rv = (RecyclerView)mView.findViewById(R.id.payment_history_rv);
        payment_history_rv.setLayoutManager(new LinearLayoutManager(mActivity));
        payment_history_rv.setAdapter(batchHistoryAdapter);

        from_ll = (LinearLayout)mView.findViewById(R.id.from_ll);
        to_ll = (LinearLayout)mView.findViewById(R.id.to_ll);
        from_ll.setOnClickListener(this);
        to_ll.setOnClickListener(this);
        from_month_tv = (TextView)mView.findViewById(R.id.from_month_tv);
        from_year_tv = (TextView)mView.findViewById(R.id.from_year_tv);
        to_month_tv = (TextView)mView.findViewById(R.id.to_month_tv);
        to_year_tv = (TextView)mView.findViewById(R.id.to_year_tv);

        search_layout = (FrameLayout)mView.findViewById(R.id.search_layout);
        search_layout.setOnClickListener(this);
    }

    private void initData() {
        mBatchList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.from_ll:
                showMonthYearDialog(from_month_tv, from_year_tv);
                break;

            case R.id.to_ll:
                showMonthYearDialog(to_month_tv, to_year_tv);
                break;

            case R.id.search_layout:

                break;
        }
    }

    private TextView month_dialog_tv , year_dialog_tv;
    private void showMonthYearDialog(final TextView month, final TextView year) {
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.month_year_dialog_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(dialogView);

        final DateChooserAdapter dateChooserAdapter = new DateChooserAdapter(mActivity);
        RecyclerView year_month_rv = (RecyclerView)dialogView.findViewById(R.id.year_month_rv);
        year_month_rv.setLayoutManager(new LinearLayoutManager(mActivity));
        year_month_rv.setAdapter(dateChooserAdapter);
        dateChooserAdapter.setDateSelectionListener(this);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                month.setText(month_dialog_tv.getText().toString());
                year.setText(year_dialog_tv.getText().toString());
//                Toast.makeText(mActivity, month_dialog_tv.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        month_dialog_tv = (TextView)dialogView.findViewById(R.id.month_dialog_tv);
        month_dialog_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChooserAdapter.setAdapterData(false);
            }
        });
        year_dialog_tv = (TextView)dialogView.findViewById(R.id.year_dialog_tv);
        year_dialog_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChooserAdapter.setAdapterData(true);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDateSelected(boolean type, String month) {
        if (type) {
            year_dialog_tv.setText(month);
        } else {
            month_dialog_tv.setText(month);
        }
    }
}
