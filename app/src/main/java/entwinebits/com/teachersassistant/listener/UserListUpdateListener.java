package entwinebits.com.teachersassistant.listener;

import java.util.ArrayList;

import entwinebits.com.teachersassistant.model.UserProfileDTO;

/**
 * Created by shajib on 12/22/2016.
 */
public interface UserListUpdateListener {

    void onUpdateUserList(ArrayList<UserProfileDTO> userList);
}
