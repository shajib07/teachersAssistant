package entwinebits.com.teachersassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shajib on 1/4/2017.
 */
public class PaymentHistoryDTO implements Parcelable {

    private int paymentId;
    private int year;
    private int month;
    private boolean paid;
    private int paidAmount;
    private int studentId;
    private int batchId;
    private String studentName;
    private boolean isFirstItem;

    public boolean isFirstItem() {
        return isFirstItem;
    }

    public void setFirstItem(boolean firstItem) {
        isFirstItem = firstItem;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public PaymentHistoryDTO(int year, int month) {
        this.year = year;
        this.month = month;
        this.paymentId = -1;
        this.paidAmount = 0;
        this.paid = false;
    }

    public PaymentHistoryDTO() {

    }

    private PaymentHistoryDTO(Parcel in) {
        paymentId = in.readInt();
        year = in.readInt();
        month = in.readInt();
        paidAmount = in.readInt();
        studentId = in.readInt();
        batchId = in.readInt();
        studentName = in.readString();
        paid = in.readInt() == 0 ? false : true ;
        isFirstItem = in.readInt() == 0 ? false : true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(paymentId);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(paidAmount);
        dest.writeInt(studentId);
        dest.writeInt(batchId);
        dest.writeString(studentName);
        dest.writeInt(paid ? 1 : 0);
        dest.writeInt(isFirstItem ? 1 : 0);
    }

    public static final Parcelable.Creator<PaymentHistoryDTO> CREATOR = new Parcelable.Creator<PaymentHistoryDTO>() {
        public PaymentHistoryDTO createFromParcel(Parcel in) {
            return new PaymentHistoryDTO(in);
        }

        public PaymentHistoryDTO[] newArray(int size) {
            return new PaymentHistoryDTO[size];
        }
    };
}
