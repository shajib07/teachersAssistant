package entwinebits.com.teachersassistant.model;

import entwinebits.com.teachersassistant.interfaces.ItemSelectionType;

/**
 * Created by Nargis Rahman on 4/20/2017.
 */
public class ItemSelectionDTO implements ItemSelectionType {

    private int itemType = 0;
    private int itemTypeId = 0;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    private String itemName ="";

    public void setItemName(String itemName){
        this.itemName =itemName;
    }

    public void setItemTypeId(int id){
        this.itemTypeId = id;
    }
    @Override
    public int getItemTypeId() {
        return itemTypeId;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public String getItemName() {
        return itemName;
    }
}
