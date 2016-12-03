package entwinebits.com.teachersassistant.fragment;

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

/**
 * Created by Nargis Rahman on 12/2/2016.
 */
public class PaymentHistoryFragment extends Fragment {

    Context context;

    public PaymentHistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_history_layout, container, false);
        context = getActivity();

        return view;

    }

}
