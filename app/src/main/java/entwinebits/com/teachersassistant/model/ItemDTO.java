package entwinebits.com.teachersassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shajib on 3/23/2017.
 */
public class ItemDTO implements Parcelable {

    private int itemId;
    private String itemName;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ItemDTO() {

    }

    private ItemDTO(Parcel in) {
        itemId = in.readInt();
        itemName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemId);
        dest.writeString(itemName);
    }

    public static final Parcelable.Creator<ItemDTO> CREATOR = new Parcelable.Creator<ItemDTO>() {
        public ItemDTO createFromParcel(Parcel in) {
            return new ItemDTO(in);
        }

        public ItemDTO[] newArray(int size) {
            return new ItemDTO[size];
        }
    };
}
