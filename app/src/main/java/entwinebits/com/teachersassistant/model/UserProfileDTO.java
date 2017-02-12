package entwinebits.com.teachersassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shajib on 8/26/2016.
 */
public class UserProfileDTO implements Parcelable{

    private int userId;
    private String userName;
    private String userPwd;
    private String userFirstName;
    private String userLastName;
    private int userGender;
    private String userCity;
    private String userCountry;
    private String userMobilePhone;
    private String userEmail;
    private String userBirthday;
    private String userAddress;
    private String userDesignation;
    private String userInstituteName;
    private boolean isTeacher;
    private int monthlyFee;
    private long batchId;

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
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

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserMobilePhone() {
        return userMobilePhone;
    }

    public void setUserMobilePhone(String userMobilePhone) {
        this.userMobilePhone = userMobilePhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public String getUserInstituteName() {
        return userInstituteName;
    }

    public void setUserInstituteName(String userInstituteName) {
        this.userInstituteName = userInstituteName;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public int getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(int monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public UserProfileDTO() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    private UserProfileDTO(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        userPwd = in.readString();
        userFirstName = in.readString();
        userLastName = in.readString();
        userGender = in.readInt();
        userCity = in.readString();
        userCountry = in.readString();
        userMobilePhone = in.readString();
        userEmail = in.readString();
        userBirthday = in.readString();
        userAddress = in.readString();
        userDesignation = in.readString();
        userInstituteName = in.readString();
        isTeacher = (in.readInt() == 0 ? false : true);
        monthlyFee = in.readInt();
        batchId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(userPwd);
        dest.writeString(userFirstName);
        dest.writeString(userLastName);
        dest.writeInt(userGender);
        dest.writeString(userCity);
        dest.writeString(userCountry);
        dest.writeString(userMobilePhone);
        dest.writeString(userEmail);
        dest.writeString(userBirthday);
        dest.writeString(userAddress);
        dest.writeString(userDesignation);
        dest.writeString(userInstituteName);
        dest.writeInt(isTeacher ? 1: 0);
        dest.writeInt(monthlyFee);
        dest.writeLong(batchId);
    }

    public static final Parcelable.Creator<UserProfileDTO> CREATOR = new Parcelable.Creator<UserProfileDTO>() {
        public UserProfileDTO createFromParcel(Parcel in) {
            return new UserProfileDTO(in);
        }

        public UserProfileDTO[] newArray(int size) {
            return new UserProfileDTO[size];
        }
    };

}
