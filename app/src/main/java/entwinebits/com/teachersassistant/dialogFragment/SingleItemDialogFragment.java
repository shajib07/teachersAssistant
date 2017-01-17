package entwinebits.com.teachersassistant.dialogFragment;

import android.content.DialogInterface;
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

import entwinebits.com.teachersassistant.PaymentHistoryActivity;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.DateChooserAdapter;
import entwinebits.com.teachersassistant.adapter.SingleItemDialogAdapter;
import entwinebits.com.teachersassistant.listener.DateSelectionListener;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 1/13/2017.
 */
public class SingleItemDialogFragment extends DialogFragment implements View.OnClickListener, DateSelectionListener {

    private String TAG = "SingleItemDialogFragment";
    private RecyclerView single_item_rv;
    private TextView first_item_tv, dialog_cancel_tv, dialog_ok_tv;
    private DialogCloseListener dialogCloseListener;
    private ArrayList<String> mItemList;
    private int dialogId;

    public SingleItemDialogFragment() {

    }

    public static SingleItemDialogFragment newInstance(int dialogId, ArrayList<String> list) {
        SingleItemDialogFragment frag = new SingleItemDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("ItemList", list);
        args.putInt("dialogId", dialogId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_item_dialog_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();
        mItemList = bundle.getStringArrayList("ItemList");
        dialogId = bundle.getInt("dialogId");
        HelperMethod.debugLog(TAG, "mItemList size : "+mItemList.size());

        dialogCloseListener = (DialogCloseListener)getActivity();
        SingleItemDialogAdapter singleItemDialogAdapter = new SingleItemDialogAdapter(getActivity(), mItemList, this);
        single_item_rv = (RecyclerView) view.findViewById(R.id.single_item_rv);
        single_item_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        single_item_rv.setAdapter(singleItemDialogAdapter);

        dialog_cancel_tv = (TextView) view.findViewById(R.id.dialog_cancel_tv);
        dialog_ok_tv = (TextView) view.findViewById(R.id.dialog_ok_tv);
        dialog_cancel_tv.setOnClickListener(this);
        dialog_ok_tv.setOnClickListener(this);

        first_item_tv = (TextView) view.findViewById(R.id.first_item_tv);
    }

    @Override
    public void onDateSelected(boolean type, String month) {
        Toast.makeText(getActivity(), "month : "+month, Toast.LENGTH_SHORT).show();
        if (type) {
            first_item_tv.setText(month);
        } else {
//            month_dialog_tv.setText(month);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok_tv:
                String selectedYear = first_item_tv.getText().toString();
//                Toast.makeText(getActivity(), "Selected " + selectedYear, Toast.LENGTH_SHORT).show();
                dialogCloseListener.onDialogClosed(dialogId, Constants.DIALOG_STATE_POSITIVE, selectedYear, "");
                dismiss();
                break;

            case R.id.dialog_cancel_tv:
//                dialogCloseListener.onDialogClosed(Constants.DIALOG_STATE_NEGATIVE);
                dismiss();
                break;
        }
    }

}
