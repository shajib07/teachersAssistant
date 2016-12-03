package entwinebits.com.teachersassistant.model;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class BatchDTO {

    private String batchName;
    private ScheduleDTO scheduleDTO;

    public ScheduleDTO getScheduleDTO() {
        return scheduleDTO;
    }

    public void setScheduleDTO(ScheduleDTO scheduleDTO) {
        this.scheduleDTO = scheduleDTO;
    }

    public String getBatchName() {

        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }
}
