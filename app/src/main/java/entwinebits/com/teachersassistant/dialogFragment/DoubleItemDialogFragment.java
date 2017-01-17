package entwinebits.com.teachersassistant.dialogFragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.SingleItemDialogAdapter;
import entwinebits.com.teachersassistant.listener.DateSelectionListener;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 1/13/2017.
 */
public class DoubleItemDialogFragment extends DialogFragment implements View.OnClickListener, DateSelectionListener {

    private String TAG = "DoubleItemDialogFragment";
    private RecyclerView dialog_item_rv;
    private TextView first_item_tv, second_item_tv, dialog_cancel_tv, dialog_ok_tv;
    private DialogCloseListener dialogCloseListener;
    private ArrayList<String> mFirstItemList, mSecondItemList;
    private boolean isFirstItem ;
    private int dialogId;

    public DoubleItemDialogFragment() {

    }

    public static DoubleItemDialogFragment newInstance(int dialogId, ArrayList<String> list, ArrayList<String> list2) {
        DoubleItemDialogFragment frag = new DoubleItemDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("FirstItemList", list);
        args.putStringArrayList("SecondItemList", list2);
        args.putInt("dialogId", dialogId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.double_item_dialog_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();
        mFirstItemList = bundle.getStringArrayList("FirstItemList");
        mSecondItemList = bundle.getStringArrayList("SecondItemList");
        dialogId = bundle.getInt("dialogId");
        HelperMethod.debugLog(TAG, "mItemList size : "+mFirstItemList.size());

        dialogCloseListener = (DialogCloseListener)getActivity();
        final SingleItemDialogAdapter singleItemDialogAdapter = new SingleItemDialogAdapter(getActivity(), new ArrayList<String>(), this);
        dialog_item_rv = (RecyclerView) view.findViewById(R.id.dialog_item_rv);
        dialog_item_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        dialog_item_rv.setAdapter(singleItemDialogAdapter);
        singleItemDialogAdapter.setAdapterData(mFirstItemList);

        dialog_cancel_tv = (TextView) view.findViewById(R.id.dialog_cancel_tv);
        dialog_ok_tv = (TextView) view.findViewById(R.id.dialog_ok_tv);
        dialog_cancel_tv.setOnClickListener(this);
        dialog_ok_tv.setOnClickListener(this);

        first_item_tv = (TextView) view.findViewById(R.id.first_item_tv);
        first_item_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstItem = true;
                singleItemDialogAdapter.setAdapterData(mFirstItemList);
            }
        });
        second_item_tv = (TextView) view.findViewById(R.id.second_item_tv);
        second_item_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstItem = false;
                singleItemDialogAdapter.setAdapterData(mSecondItemList);
            }
        });
        if (mFirstItemList != null && mFirstItemList.size() > 0) {
            first_item_tv.setText(mFirstItemList.get(0));
        }
        if (mSecondItemList != null && mSecondItemList.size() > 0) {
            second_item_tv.setText(mSecondItemList.get(0));
        }
    }

    @Override
    public void onDateSelected(boolean type, String item) {
        Toast.makeText(getActivity(), "item : "+item, Toast.LENGTH_SHORT).show();
        if (isFirstItem) {
            first_item_tv.setText(item);
        } else {
            second_item_tv.setText(item);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok_tv:
                String selectedYear = first_item_tv.getText().toString();
                String selectedMonth = second_item_tv.getText().toString();
//                Toast.makeText(getActivity(), "Selected " + selectedYear, Toast.LENGTH_SHORT).show();
                dialogCloseListener.onDialogClosed(dialogId, Constants.DIALOG_STATE_POSITIVE, selectedYear, selectedMonth);
                dismiss();
                break;

            case R.id.dialog_cancel_tv:
//                dialogCloseListener.onDialogClosed(Constants.DIALOG_STATE_NEGATIVE);
                dismiss();
                break;
        }
    }

}
