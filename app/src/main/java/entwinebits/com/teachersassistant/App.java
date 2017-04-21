package entwinebits.com.teachersassistant;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by shajib on 4/20/2017.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        App.mContext = getApplicationContext();
        Toast.makeText(this, "APP onCreate ", Toast.LENGTH_SHORT).show();
    }

    public static Context getContext() {
        return mContext;
    }
}
