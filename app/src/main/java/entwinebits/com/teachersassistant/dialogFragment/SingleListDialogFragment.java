package entwinebits.com.teachersassistant.dialogFragment;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.SingleItemDialogAdapter;
import entwinebits.com.teachersassistant.adapter.SingleListDialogAdapter;
import entwinebits.com.teachersassistant.listener.DateSelectionListener;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.model.ItemDTO;
import entwinebits.com.teachersassistant.utils.Constants;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by shajib on 3/23/2017.
 */
public class SingleListDialogFragment extends DialogFragment implements View.OnClickListener, DateSelectionListener {

    private String TAG = "SingleItemDialogFragment";
    private RecyclerView single_item_rv;
    private TextView first_item_tv;
    private Button dialog_cancel_btn, dialog_ok_btn;
    private DialogCloseListener dialogCloseListener;
    private ArrayList<ItemDTO> mItemList;
    private int dialogId;
    private int mSelectedItemId;
    private String mSelectedItemName;

    public SingleListDialogFragment() {

    }

    public static SingleListDialogFragment newInstance(int dialogId, ArrayList<ItemDTO> list) {
        SingleListDialogFragment frag = new SingleListDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("ItemList", list);
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
        mItemList = bundle.getParcelableArrayList("ItemList");
        dialogId = bundle.getInt("dialogId");
        HelperMethod.debugLog(TAG, "mItemList size : "+mItemList.size());

        dialogCloseListener = (DialogCloseListener)getActivity();
        SingleListDialogAdapter singleListDialogAdapter = new SingleListDialogAdapter(mItemList, this);
        single_item_rv = (RecyclerView) view.findViewById(R.id.single_item_rv);
        single_item_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        single_item_rv.setAdapter(singleListDialogAdapter);

        dialog_cancel_btn = (Button) view.findViewById(R.id.dialog_cancel_btn);
        dialog_ok_btn = (Button) view.findViewById(R.id.dialog_ok_btn);
        dialog_cancel_btn.setOnClickListener(this);
        dialog_ok_btn.setOnClickListener(this);

        first_item_tv = (TextView) view.findViewById(R.id.first_item_tv);
    }

    @Override
    public void onDateSelected(boolean type, String month, int id) {
        Toast.makeText(getActivity(), "month : "+month, Toast.LENGTH_SHORT).show();
        if (type) {
            mSelectedItemId = id;
            mSelectedItemName = month;
            first_item_tv.setText(month);
        } else {
//            month_dialog_tv.setText(month);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok_btn:
                String selectedYear = first_item_tv.getText().toString();

//                Toast.makeText(getActivity(), "Selected " + selectedYear, Toast.LENGTH_SHORT).show();
                dialogCloseListener.onDialogClosed(dialogId, Constants.DIALOG_STATE_POSITIVE, mSelectedItemName, "", mSelectedItemId);
                dismiss();
                break;

            case R.id.dialog_cancel_btn:
//                dialogCloseListener.onDialogClosed(Constants.DIALOG_STATE_NEGATIVE);
                dismiss();
                break;
        }
    }

}
