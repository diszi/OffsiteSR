package hu.d2.offsitesr.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by csabinko on 2017.09.18..
 */

public class EnvironmentTool {

    public static void changeLanguage(Context context, String code) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = new Locale(code.toLowerCase());
        resources.updateConfiguration(conf, displayMetrics);
    }

    public static String getCurrentDateString(){
        return DateFormat.getDateTimeInstance().format(new Date());

    }
}
