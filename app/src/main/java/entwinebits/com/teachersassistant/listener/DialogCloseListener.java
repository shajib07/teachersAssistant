package entwinebits.com.teachersassistant.listener;

/**
 * Created by shajib on 1/13/2017.
 */
public interface DialogCloseListener {

    void onDialogClosed(int dialogId, int dialogState, String year, String month);
}
