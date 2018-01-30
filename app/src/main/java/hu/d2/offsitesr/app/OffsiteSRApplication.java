package hu.d2.offsitesr.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;


/**
 * Created by csabinko on 2017.09.14..
 */

public class OffsiteSRApplication extends Application {

    private static Context appContext;
    public static Context getAppContext(){
        return OffsiteSRApplication.appContext;
    }

    /**
     * Called when the application is starting, before any activity,
     * service, or receiver objects have been created.
     */
    @Override
    public void onCreate() {

        super.onCreate();
        OffsiteSRApplication.appContext = getApplicationContext();

    }

    /**
     * Called by the system when the device configuration
     * changes while your component is running.
     * @param newConfig - the new device configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }




}
