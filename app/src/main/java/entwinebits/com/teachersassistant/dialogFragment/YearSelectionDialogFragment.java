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

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.DateChooserAdapter;
import entwinebits.com.teachersassistant.listener.DateSelectionListener;
import entwinebits.com.teachersassistant.listener.DialogCloseListener;
import entwinebits.com.teachersassistant.utils.Constants;

/**
 * Created by shajib on 1/13/2017.
 */
public class YearSelectionDialogFragment extends DialogFragment implements DialogInterface.OnDismissListener, View.OnClickListener, DateSelectionListener {

    private RecyclerView year_month_rv;
    private TextView year_dialog_tv, dialog_cancel_tv, dialog_ok_tv;
    private DialogCloseListener dialogCloseListener;

    public YearSelectionDialogFragment() {

    }

    public static YearSelectionDialogFragment newInstance(String title) {
        YearSelectionDialogFragment frag = new YearSelectionDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.month_year_dialog_view, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCloseListener = (DialogCloseListener)getActivity();
        final DateChooserAdapter dateChooserAdapter = new DateChooserAdapter(getActivity());
        year_month_rv = (RecyclerView) view.findViewById(R.id.year_month_rv);
        year_month_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        year_month_rv.setAdapter(dateChooserAdapter);
        dateChooserAdapter.setDateSelectionListener(this);
        dateChooserAdapter.setAdapterData(true);

        dialog_cancel_tv = (TextView) view.findViewById(R.id.dialog_cancel_tv);
        dialog_ok_tv = (TextView) view.findViewById(R.id.dialog_ok_tv);
        dialog_cancel_tv.setOnClickListener(this);
        dialog_ok_tv.setOnClickListener(this);

        view.findViewById(R.id.month_dialog_tv).setVisibility(View.GONE);

        year_dialog_tv = (TextView) view.findViewById(R.id.year_dialog_tv);

    }

    @Override
    public void onDateSelected(boolean type, String month) {
        if (type) {
            year_dialog_tv.setText(month);
        } else {
//            month_dialog_tv.setText(month);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok_tv:
                String selectedYear = year_dialog_tv.getText().toString();
//                Toast.makeText(getActivity(), "Selected " + selectedYear, Toast.LENGTH_SHORT).show();
                dialogCloseListener.onDialogClosed(Constants.DIALOG_STATE_POSITIVE, selectedYear, "");
                dismiss();
                break;

            case R.id.dialog_cancel_tv:
//                dialogCloseListener.onDialogClosed(Constants.DIALOG_STATE_NEGATIVE);
                dismiss();
                break;
        }
    }

}
