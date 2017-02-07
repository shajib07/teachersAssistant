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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import entwinebits.com.teachersassistant.db.DatabaseRequestHelper;
import entwinebits.com.teachersassistant.listener.DateSelectionListener;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.BatchPaymentHistoryAdapter;
import entwinebits.com.teachersassistant.adapter.DateChooserAdapter;
import entwinebits.com.teachersassistant.model.BatchDTO;
import entwinebits.com.teachersassistant.model.PaymentDTO;
import entwinebits.com.teachersassistant.model.UserProfileDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;

/**
 * Created by Nargis Rahman on 12/2/2016.
 */
public class PaymentHistoryFragment extends Fragment implements View.OnClickListener, DateSelectionListener {

    private String TAG = "PaymentHistoryFragment";
    private Activity mActivity;
    private View mView;
    private RecyclerView payment_history_rv;
    private ArrayList<PaymentDTO> mPaymentHistoryList;
    private ArrayList<PaymentDTO> mAllBatchHistoryList;

    private ArrayList<BatchDTO> mBatchList;
    private BatchPaymentHistoryAdapter mPaymentHistoryAdapter;

    private LinearLayout from_ll, to_ll;
    private TextView from_month_tv, to_month_tv, from_year_tv, to_year_tv;
    private Calendar mCalendar;
    private FrameLayout search_layout;
    private Spinner mSpinner;
    private HashMap<Long, String> mSpinnerMap;
    private DatabaseRequestHelper mDbRequestHelper;


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

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (mDbRequestHelper == null) {
            mDbRequestHelper = new DatabaseRequestHelper(mActivity);
        }

        mAllBatchHistoryList.clear();
        mSpinnerMap.clear();
        mSpinnerMap.put((long) -1, "All");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBatchList = mDbRequestHelper.getBatchList();

                for (BatchDTO batchDTO : mBatchList) {
                    HelperMethod.debugLog(TAG, "load == "+batchDTO.getBatchId()+ " namm = "+batchDTO.getBatchName());

                    int total_amount = 0;
                    ArrayList<UserProfileDTO> studentList = mDbRequestHelper.getStudentListByBatch((int)batchDTO.getBatchId());
                    for (UserProfileDTO dto : studentList) {
                        total_amount += dto.getMonthlyFee();
                    }

                    PaymentDTO paymentDTO = new PaymentDTO();
                    paymentDTO.setTotalAmount(total_amount);
                    paymentDTO.setBatchName(batchDTO.getBatchName());
                    mAllBatchHistoryList.add(paymentDTO);
                    mSpinnerMap.put(batchDTO.getBatchId(), batchDTO.getBatchName());
                }

                mPaymentHistoryList.clear();
                mPaymentHistoryList.addAll(mAllBatchHistoryList);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPaymentHistoryAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    private void loadSpinnerData(int i) {

        for (Map.Entry<Long, String> entry : mSpinnerMap.entrySet()) {
            HelperMethod.debugLog(TAG, "key = " +entry.getKey()+ "val = " +entry.getValue()+ " from == "+i);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, new ArrayList(mSpinnerMap.values()));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
    }

    private void initLayout() {
        mPaymentHistoryAdapter = new BatchPaymentHistoryAdapter(mActivity, mPaymentHistoryList);
        payment_history_rv = (RecyclerView)mView.findViewById(R.id.payment_history_rv);
        payment_history_rv.setLayoutManager(new LinearLayoutManager(mActivity));
        payment_history_rv.setAdapter(mPaymentHistoryAdapter);

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

        mSpinner = (Spinner) mView.findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                HelperMethod.debugLog(TAG, "onItemSelected ==  "+item);

                if (item.equalsIgnoreCase("All")) {
                    HelperMethod.debugLog(TAG, "before processAllBatchHistory +++ ");
                    processAllBatchHistory();
                    return;
                }
                long currentBatchId = 0;
                for (Map.Entry<Long, String> entry : mSpinnerMap.entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(item)) {
                        currentBatchId = entry.getKey();
                        break;
                    }
                }

                final long finalCurrentBatchId = currentBatchId;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<PaymentDTO> paymentDTOs = new ArrayList<>();
                        if (mDbRequestHelper == null) {
                            mDbRequestHelper = new DatabaseRequestHelper(mActivity);
                        }
                        ArrayList<UserProfileDTO> studentList = mDbRequestHelper.getStudentListByBatch((int) finalCurrentBatchId);

                        for (UserProfileDTO dto : studentList) {
                            PaymentDTO paymentDTO = new PaymentDTO();
                            paymentDTO.setTotalAmount(dto.getMonthlyFee());
                            paymentDTO.setBatchName(dto.getUserName());
                            paymentDTOs.add(paymentDTO);
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPaymentHistoryList.clear();
                                mPaymentHistoryList.addAll(paymentDTOs);
                                mPaymentHistoryAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                }).start();

                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void processAllBatchHistory() {


        if (mAllBatchHistoryList.size() > 0) {
            mPaymentHistoryList.clear();
            mPaymentHistoryList.addAll(mAllBatchHistoryList);
            mPaymentHistoryAdapter.notifyDataSetChanged();
            return;
        }

        if (mDbRequestHelper == null) {
            mDbRequestHelper = new DatabaseRequestHelper(mActivity);
        }

        if (mAllBatchHistoryList == null) {
            mAllBatchHistoryList = new ArrayList<>();
        } else {
            mAllBatchHistoryList.clear();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBatchList = mDbRequestHelper.getBatchList();

                for (BatchDTO batchDTO : mBatchList) {
                    int total_amount = 0;
                    ArrayList<UserProfileDTO> studentList = mDbRequestHelper.getStudentListByBatch((int)batchDTO.getBatchId());
                    for (UserProfileDTO dto : studentList) {
                        total_amount += dto.getMonthlyFee();
                    }

                    PaymentDTO paymentDTO = new PaymentDTO();
                    paymentDTO.setTotalAmount(total_amount);
                    paymentDTO.setBatchName(batchDTO.getBatchName());
                    mAllBatchHistoryList.add(paymentDTO);
                    mSpinnerMap.put(batchDTO.getBatchId(), batchDTO.getBatchName());
                }

                mPaymentHistoryList.clear();
                mPaymentHistoryList.addAll(mAllBatchHistoryList);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPaymentHistoryAdapter.notifyDataSetChanged();
                        loadSpinnerData(1);
                    }
                });
            }
        }).start();

    }

    private void initData() {
        mPaymentHistoryList = new ArrayList<>();
        mAllBatchHistoryList = new ArrayList<>();
        mBatchList = new ArrayList<>();
        mSpinnerMap = new LinkedHashMap<>();
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
