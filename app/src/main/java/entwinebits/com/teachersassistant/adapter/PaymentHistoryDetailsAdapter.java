package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.model.PaymentHistoryDTO;
import entwinebits.com.teachersassistant.utils.HelperMethod;
import entwinebits.com.teachersassistant.utils.Months;

/**
 * Created by shajib on 3/26/2017.
 */
public class PaymentHistoryDetailsAdapter extends RecyclerView.Adapter<PaymentHistoryDetailsAdapter.PaymentDetailsHolder> {

    private String TAG ="PaymentHistoryDetailsAdapter";
    private Activity mActivity;
    private ArrayList<String> mMonthList;
    private ArrayList<PaymentHistoryDTO> mPaymentHistoryList;
    private ArrayList<PaymentHistoryDTO> mReceivedDataList;

//    private ArrayList<Integer> mHeaderList;
    private PaymentEditSelectionListener paymentEditSelectionListener;

    public interface PaymentEditSelectionListener {
        void onPaymentEdited(PaymentHistoryDTO dto);
    }

    public PaymentHistoryDetailsAdapter(Activity activity, ArrayList<PaymentHistoryDTO> list) {
        this.mActivity = activity;
        getMonthList();
        this.mReceivedDataList = list;
        this.mPaymentHistoryList = getProcessedList(list);
//        mHeaderList = new ArrayList<>();
    }

    public void setPaymentEditSelectionListener(PaymentEditSelectionListener listener) {
        this.paymentEditSelectionListener = listener;
    }

    public ArrayList<PaymentHistoryDTO> getProcessedList(ArrayList<PaymentHistoryDTO> list) {
        int yr = 0;
        for (int i=0; i< list.size(); i++) {
            if (list.get(i).getYear() != yr) {
                list.get(i).setFirstItem(true);
                yr = list.get(i).getYear();
                HelperMethod.debugLog(TAG, "First = "+yr);
            } else {
                list.get(i).setFirstItem(false);
            }
        }
        return list;
    }

    public void notifyDataAdapter(final int year, final int month, final int amount) {
        HelperMethod.debugLog(TAG, "yr "+year+" month "+month+" a "+amount);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (PaymentHistoryDTO dto : mReceivedDataList) {
                    if (dto.getYear() == year && dto.getMonth() == month) {
                        dto.setPaidAmount(amount);
                        break;
                    }
                }

                ArrayList<PaymentHistoryDTO> tempList = getProcessedList(mReceivedDataList);
                mPaymentHistoryList.clear();
                mPaymentHistoryList.addAll(tempList);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    private void getMonthList() {
        mMonthList = new ArrayList<>();
        String[] arr = mActivity.getResources().getStringArray(R.array.months);
        for (int i=0; i < arr.length; i++) {
            mMonthList.add(arr[i]);
        }
    }

    @Override
    public PaymentDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_details_single_item, parent, false);
        return new PaymentDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(final PaymentDetailsHolder holder, int position) {
        final PaymentHistoryDTO dto = mPaymentHistoryList.get(position);
        if (dto.isFirstItem()) {
            holder.payment_header_tv.setVisibility(View.VISIBLE);
            holder.payment_header_tv.setText("" + dto.getYear());
            holder.payment_header_line.setVisibility(View.VISIBLE);
        } else {
            holder.payment_header_line.setVisibility(View.GONE);
            holder.payment_header_tv.setVisibility(View.GONE);
        }

        holder.payment_item_month.setText("" + Months.get(dto.getMonth()));
        if (dto.isPaid()) {
            holder.payment_row_ll.setBackgroundColor(mActivity.getResources().getColor(R.color.paid_row_color));
        } else {
            holder.payment_row_ll.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
        }
        holder.payment_item_paid.setText(dto.getPaidAmount() + "");
        holder.payment_item_due.setText("0");

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                HelperMethod.debugLog(TAG, ""+dto.getPaidAmount()+" "+dto.getPaymentId());
                paymentEditSelectionListener.onPaymentEdited(dto);
//                showDoubleOptionDialog(mActivity, dto);
//                holder.payment_item_paid_et.setVisibility(View.VISIBLE);
//                holder.payment_item_due_et.setVisibility(View.VISIBLE);
//                holder.payment_item_paid.setVisibility(View.GONE);
//                holder.payment_item_due.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPaymentHistoryList == null ? 0 : mPaymentHistoryList.size();
    }

    public class PaymentDetailsHolder extends RecyclerView.ViewHolder {
        private TextView payment_item_month, payment_item_paid, payment_item_due, payment_header_tv;
        private LinearLayout payment_row_ll;
        private View payment_header_line;
        private EditText payment_item_due_et, payment_item_paid_et;
        public PaymentDetailsHolder(View itemView) {
            super(itemView);
            payment_row_ll = (LinearLayout) itemView.findViewById(R.id.payment_row_ll);
            payment_header_tv = (TextView) itemView.findViewById(R.id.payment_header_tv);
            payment_item_month = (TextView) itemView.findViewById(R.id.payment_item_month);
            payment_item_paid = (TextView) itemView.findViewById(R.id.payment_item_paid);
            payment_item_due = (TextView) itemView.findViewById(R.id.payment_item_due);
            payment_item_paid_et = (EditText) itemView.findViewById(R.id.payment_item_paid_et);
            payment_item_due_et = (EditText) itemView.findViewById(R.id.payment_item_due_et);
            payment_header_line = itemView.findViewById(R.id.payment_header_line);

        }
    }
}
