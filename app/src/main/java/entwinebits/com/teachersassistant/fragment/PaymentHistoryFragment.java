package entwinebits.com.teachersassistant.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.BatchPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.model.BatchDTO;

/**
 * Created by Nargis Rahman on 12/2/2016.
 */
public class PaymentHistoryFragment extends Fragment {

    private Activity mActivity;
    private View mView;
    private RecyclerView payment_history_rv;
    private ArrayList<BatchDTO> mBatchList;

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
        return mView;
    }

    private void initLayout() {
        BatchPaymentHistoryAdapter batchHistoryAdapter = new BatchPaymentHistoryAdapter(mActivity);
        payment_history_rv = (RecyclerView)mView.findViewById(R.id.payment_history_rv);
        payment_history_rv.setLayoutManager(new LinearLayoutManager(mActivity));
        payment_history_rv.setAdapter(batchHistoryAdapter);
    }

    private void initData() {
        mBatchList = new ArrayList<>();
    }
}
