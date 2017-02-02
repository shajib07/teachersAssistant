package entwinebits.com.teachersassistant.system;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * Created by shajib on 2/2/2017.
 */
public class DeviceConfig {

    private Activity mActivity;
    public DeviceConfig(Activity activity) {
        this.mActivity = activity;
    }
    public void getDeviceDensity() {

        int density = mActivity.getResources().getDisplayMetrics().densityDpi;

        switch (density) {

            case DisplayMetrics.DENSITY_LOW:
                Toast.makeText(mActivity, "LDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Toast.makeText(mActivity, "MDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(mActivity, "HDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Toast.makeText(mActivity, "XHDPI", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
