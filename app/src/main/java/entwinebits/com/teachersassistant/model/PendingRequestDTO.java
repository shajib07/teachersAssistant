package entwinebits.com.teachersassistant.model;

/**
 * Created by shajib on 4/7/2017.
 */
public class PendingRequestDTO {

    private String batchTitle;
    private int batchId;
    private int userBatchId;
    private int userId;
    private String userName;

    public String getBatchTitle() {
        return batchTitle;
    }

    public void setBatchTitle(String batchTitle) {
        this.batchTitle = batchTitle;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getUserBatchId() {
        return userBatchId;
    }

    public void setUserBatchId(int userBatchId) {
        this.userBatchId = userBatchId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
