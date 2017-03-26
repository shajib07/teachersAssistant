package entwinebits.com.teachersassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
//    private ArrayList<Integer> mHeaderList;

    public PaymentHistoryDetailsAdapter(Activity activity, ArrayList<PaymentHistoryDTO> list) {
        this.mActivity = activity;
        getMonthList();
        this.mPaymentHistoryList = getProcessedList(list);
//        mHeaderList = new ArrayList<>();
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
    public void onBindViewHolder(PaymentDetailsHolder holder, int position) {
        PaymentHistoryDTO dto = mPaymentHistoryList.get(position);
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
        }
        holder.payment_item_paid.setText(dto.getPaidAmount() + "");
        holder.payment_item_due.setText("0");
    }

    @Override
    public int getItemCount() {
        return mPaymentHistoryList == null ? 0 : mPaymentHistoryList.size();
    }

    public class PaymentDetailsHolder extends RecyclerView.ViewHolder {
        private TextView payment_item_month, payment_item_paid, payment_item_due, payment_header_tv;
        private LinearLayout payment_row_ll;
        private View payment_header_line;
        public PaymentDetailsHolder(View itemView) {
            super(itemView);
            payment_row_ll = (LinearLayout) itemView.findViewById(R.id.payment_row_ll);
            payment_header_tv = (TextView) itemView.findViewById(R.id.payment_header_tv);
            payment_item_month = (TextView) itemView.findViewById(R.id.payment_item_month);
            payment_item_paid = (TextView) itemView.findViewById(R.id.payment_item_paid);
            payment_item_due = (TextView) itemView.findViewById(R.id.payment_item_due);
            payment_header_line = itemView.findViewById(R.id.payment_header_line);

        }
    }
}
