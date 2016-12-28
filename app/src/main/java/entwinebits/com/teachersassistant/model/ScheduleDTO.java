package entwinebits.com.teachersassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Nargis Rahman on 9/2/2016.
 */
public class ScheduleDTO implements Parcelable {

    public ScheduleDTO() {}

    private long batchId;
    private long scheduleId;
    private long routineId;
    private int daysOfWeek;
    private String startTime;
    private String endTime;
    private long startDate;
    private long endDate;
    private boolean scheduleStatus;

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public long getRoutineId() {
        return routineId;
    }

    public void setRoutineId(long routineId) {
        this.routineId = routineId;
    }

    public int getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(int daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(boolean scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private ScheduleDTO(Parcel in) {
        batchId = in.readLong();
        scheduleId = in.readLong();
        routineId = in.readLong();
        daysOfWeek = in.readInt();
        startTime = in.readString();
        endTime = in.readString();
        startDate = in.readLong();
        endDate = in.readLong();
        scheduleStatus = (in.readInt() == 0 ? false : true);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(batchId);
        dest.writeLong(scheduleId);
        dest.writeLong(routineId);
        dest.writeInt(daysOfWeek);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        dest.writeInt(scheduleStatus ? 1 : 0);
    }

    public static final Parcelable.Creator<ScheduleDTO> CREATOR = new Parcelable.Creator<ScheduleDTO>() {
        public ScheduleDTO createFromParcel(Parcel in) {
            return new ScheduleDTO(in);
        }

        public ScheduleDTO[] newArray(int size) {
            return new ScheduleDTO[size];
        }
    };

}
