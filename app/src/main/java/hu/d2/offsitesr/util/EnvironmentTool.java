package hu.d2.offsitesr.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.LocaleList;
import android.provider.Settings;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;




import hu.d2.offsitesr.BuildConfig;
import hu.d2.offsitesr.app.OffsiteSRApplication;
import hu.d2.offsitesr.app.singleton.HolderSingleton;

/**
 * Created by csabinko on 2017.09.18..
 */

public class EnvironmentTool {


    public static void setLanguage(Context context, String languageCode) {
        if (languageCode != null && !languageCode.equals("") ){
            Resources resources = context.getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration conf = resources.getConfiguration();
            Locale locale = new Locale(languageCode.toLowerCase());
            Locale.setDefault(locale);
            conf.setLocale(locale);

            resources.updateConfiguration(conf, displayMetrics);
            initApp();
        }

    }


    public static void initApp(){
        HolderSingleton.getInstance().createTicketStatusMap();
        HolderSingleton.getInstance().createPriorityMaps();
        HolderSingleton.getInstance().createTaskStatusMap();
    }


    public static void setScreenLockOff(Activity mySettingsActivity){
        Log.d("------------------>","Screen lock - OFF");
        mySettingsActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    public static void setScreenLockOn(Activity mySettingsActivity){
        Log.d("------------------>","Screen lock - ON");
        mySettingsActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    /**
     *
     * @param createdDate - created date of worklog + attachment + report + status
     * @return
     */
    public static String convertDateString(String createdDate){
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss"); //datePattern
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy.MM.dd. hh:mm");
        Date destDate  = null;
        try{
            destDate = inFormat.parse(createdDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outFormat.format(destDate);
    }

    /**
     *   - set name of picture
     *
     */
    public static String convertDate(Date date){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
    }

    /**
     *
     *  - convert actual date in string with pattern: yyyy-MM-dd'T'hh:mm:ss
     *  - updateStatusSOAP
     *
     * @return
     */
    public static String setUpdateDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(date);
    }


    //get app size
    public static String getAppSize(){
       PackageManager pm = OffsiteSRApplication.getAppContext().getPackageManager();
       File f = new File(OffsiteSRApplication.getAppContext().getApplicationInfo().publicSourceDir);

        DecimalFormat df = new DecimalFormat("#.##");
        float file_size = Float.parseFloat(String.valueOf(f.length()));
        float file_sizeKb = file_size/1024;
        float file_sizeMb = file_sizeKb/1024;

        String app_sizeMb = df.format(file_sizeMb); //MB format 1.13 Mb

       return app_sizeMb;
    }


    /**
     *
     *  - pressed date in license - get date when user press no button in updatedialog
     *  - date in about tab - get date of application update
     *
     * @param date -
     * @return  - string
     */
    public static String dateInString(Date date){
        String dateInString;
        DateFormat df  = new SimpleDateFormat("yyyy.MM.d. hh:mm");
        dateInString = df.format(date);
        return dateInString;
    }


    //get app version
    public static String getVersionApp(){
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        return versionName;

    }
    //get app name
    public static String getAppName(){
        System.out.println("aktualis appname = "+OffsiteSRApplication.getAppContext().getPackageName());
        return  OffsiteSRApplication.getAppContext().getPackageName();


    }
}
