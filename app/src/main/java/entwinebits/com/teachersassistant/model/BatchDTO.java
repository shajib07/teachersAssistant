package entwinebits.com.teachersassistant.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nargis Rahman on 9/2/2016.
 */
public class BatchDTO {
    private long batchId;
    private String batchName;
    private long routineId;
    private long courseId;
    private int numberOfStudents;
    private ArrayList<ScheduleDTO> scheduleDTOList;
    private ArrayList<UserProfileDTO> studentDtoList;

    public ArrayList<UserProfileDTO> getStudentDtoList() {
        return studentDtoList;
    }

    public void setStudentDtoList(ArrayList<UserProfileDTO> studentDtoList) {
        this.studentDtoList = studentDtoList;
    }

    public ArrayList<ScheduleDTO> getScheduleDTOList() {
        return scheduleDTOList;
    }

    public void setScheduleDTOList(ArrayList<ScheduleDTO> scheduleDTOList) {
        this.scheduleDTOList = scheduleDTOList;
    }

    private RoutineDTO routineDTO;

    public RoutineDTO getRoutineDTO() {
        return routineDTO;
    }

    public void setRoutineDTO(RoutineDTO routineDTO) {
        this.routineDTO = routineDTO;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public long getRoutineId() {
        return routineId;
    }

    public void setRoutineId(long routineId) {
        this.routineId = routineId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
}
