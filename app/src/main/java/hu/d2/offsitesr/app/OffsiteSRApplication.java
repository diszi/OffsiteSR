package hu.d2.offsitesr.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.ui.view.settings.SettingsActivity;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;

/**
 * Created by csabinko on 2017.09.14..
 */

public class OffsiteSRApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {

        super.onCreate();
        OffsiteSRApplication.appContext = getApplicationContext();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    public static Context getAppContext(){
        return OffsiteSRApplication.appContext;
    }


}
