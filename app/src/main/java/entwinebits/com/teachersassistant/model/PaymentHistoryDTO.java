package entwinebits.com.teachersassistant.model;

/**
 * Created by shajib on 1/4/2017.
 */
public class PaymentHistoryDTO {
    private int year;
    private int month;
    private boolean paid;
    private int paidAmount;
    private int studentId;

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
}