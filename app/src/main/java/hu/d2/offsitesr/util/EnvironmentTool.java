package hu.d2.offsitesr.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import hu.d2.offsitesr.BuildConfig;
import hu.d2.offsitesr.app.OffsiteSRApplication;
import hu.d2.offsitesr.app.singleton.HolderSingleton;


/**
 * Created by csabinko on 2017.09.18..
 */

public class EnvironmentTool {


    /**
     * @param context - actual activity context
     * @param languageCode - selected language (HU / ENG)
     *  - set application language
     */
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


    /**
     * @param mySettingsActivity - actual activity/page, where the user is
     *  - set screen lock activation for @param mySettingsActivity  (On / Off)
     */
    public static void setScreenLockOff(Activity mySettingsActivity){
        Log.d("------------------>","Screen lock - OFF");
        mySettingsActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void setScreenLockOn(Activity mySettingsActivity){
        Log.d("------------------>","Screen lock - ON");
        mySettingsActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * @param createdDate - created date of worklog + attachment + report + status
     * @return @param in String format using pattern DATE_PATTERN_HU
     * This method changes pattern of date
     */
    public static String convertDateString(String createdDate){
        SimpleDateFormat inFormat = new SimpleDateFormat(UIConstans.DATE_PATTERN_STANDARD); //datePattern
        SimpleDateFormat outFormat = new SimpleDateFormat(UIConstans.DATE_PATTERN_HU);
        Date destDate  = null;
        try{
            destDate = inFormat.parse(createdDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outFormat.format(destDate);
    }

    /**
     * @param date
     * @param pattern
     * @return @param date in String format using @param pattern
     */
    public static String convertDate(Date date,String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }



    //get application size in Mb
    public static String getAppSize(){
       File f = new File(OffsiteSRApplication.getAppContext().getApplicationInfo().publicSourceDir);
       DecimalFormat df = new DecimalFormat("#.##");

       float file_size = Float.parseFloat(String.valueOf(f.length()));
       float file_sizeKb = file_size/1024;
       float file_sizeMb = file_sizeKb/1024;

       String app_sizeMb = df.format(file_sizeMb);
       return app_sizeMb;
    }


    //get app version
    public static String getVersionApp(){
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        return versionName;
    }

    //get app package name
    public static String getAppPackageName(){
        return  OffsiteSRApplication.getAppContext().getPackageName();
    }

    /**
     * @param todayInString - actual date in string format
     * @return - true (If deadline > @param todayInString) / false
     * If deadline > @param todayInString  => return true
     * If deadline < @param todayInStrin => return false
     */
    public static boolean deadLineVerification(String todayInString)  {

        String deadlineInString = "2018.03.01. 11:59";
        Date today = null,deadline = null;
        DateFormat outFormat = new SimpleDateFormat(UIConstans.DATE_PATTERN_HU);
        try {
            today = outFormat.parse(todayInString);
            deadline = outFormat.parse(deadlineInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (today.after(deadline)){
            return false;
        }
        return true;

    }


}
