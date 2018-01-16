package hu.d2.offsitesr.app.singleton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.renderscript.ScriptIntrinsicYuvToRGB;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hu.d2.offsitesr.BuildConfig;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.remote.GetTicketListSOAP;
import hu.d2.offsitesr.ui.view.component.UpdateAppDialog;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListActivity;
import hu.d2.offsitesr.ui.view.verifications.UpdateActivity;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;

/**
 * Created by csabinko on 2017.09.19..
 *
 * Settings task
 *
 */

public class SettingsSingleton {
    private static final SettingsSingleton ourInstance = new SettingsSingleton();

    private String[] selectedStatuses;



    private Context context;
    private String fileConstant;
    private SharedPreferences sharedPreferences;

    private SettingsSingleton() {}

    public static SettingsSingleton getInstance() {
        return ourInstance;
    }

    public String getFileConstant() {
        return fileConstant;
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }


    public String getUserName(){
        return sharedPreferences.getString(UIConstans.LOGGED_IN_USER,null);
    }

    public String getLanguage(){

        return sharedPreferences.getString(context.getString(R.string.SettingsLanguageKey),null);
    }

    public String[] getSelectedStatus()
    {
        Set<String> selections = sharedPreferences.getStringSet(context.getString(R.string.SettingsStatusQueryKey),null);
        if (selections == null){
            selectedStatuses = null;
        }else
        {
            selectedStatuses = selections.toArray(new String[]{});
        }

        return  selectedStatuses;

    }

    public String getMaxListValue(){
        return sharedPreferences.getString(context.getString(R.string.SettingsMaxListItemsKey),null);
    }

   public String getTicketSynchronization() {
        return sharedPreferences.getString(context.getString(R.string.SettingsSynchronizeTicketListKey), null);
    }

    public String getTimeOutValue(){
        return sharedPreferences.getString(context.getString(R.string.SettingsTimeOutKey),null);
    }

    public boolean getScreenLockValue(){
       return  sharedPreferences.getBoolean(context.getString(R.string.SettingsScreenLockKey),false);
    }

    public String getSizeOfDownloadedFile(){
        return  sharedPreferences.getString(context.getString(R.string.SettingsDownloadedFileSizeKey),null);
    }

    public String getDate(){
        //System.out.println("----------------------------->  getDate in sharedPreference file = "+sharedPreferences.getString(context.getString(R.string.sharedPreference_getDate),null));
        return sharedPreferences.getString(context.getString(R.string.sharedPreference_getDate),null);
    }

    public String getWebUrl(){
       // System.out.println(" -----------------------------> web url = "+sharedPreferences.getString(context.getString(R.string.sharedPreference_addFileWebUrl),null));
        return sharedPreferences.getString(context.getString(R.string.sharedPreference_addFileWebUrl),null);
    }

    public String getDateOfAppUpdate(){
       // System.out.println("----------------->  date of app update = "+sharedPreferences.getString(context.getString(R.string.InfoAppDateofUpdateKey),null));
        return sharedPreferences.getString(context.getString(R.string.InfoAppDateofUpdateKey),null);
    }

    public String getSizeOfApp(){
        //System.out.println(" ----------------- > size of app - sharedpreference = "+sharedPreferences.getString(context.getString(R.string.InfoAppSizeKey),null));
        return sharedPreferences.getString(context.getString(R.string.InfoAppSizeKey),null);
    }
    /**
     *  - property file initialization with default values
     */
    public void init(Context context, String userName){
        this.context = context;

        this.fileConstant  = UIConstans.APP_NAME+userName;
        sharedPreferences = context.getSharedPreferences(fileConstant, Context.MODE_PRIVATE);
        setSharedPreferences(sharedPreferences);
        sharedPreferences.edit().putString(UIConstans.LOGGED_IN_USER, userName).commit();
        if (getLanguage() == null) {
            setDefaultStringValue(sharedPreferences,R.string.SettingsLanguageKey, context.getString(R.string.defaultValue_language));

        }
        if (getSelectedStatus() == null) {
            setDefaultStringSetValue(sharedPreferences,R.string.SettingsStatusQueryKey,new HashSet<String>(Arrays.asList(context.getResources().getStringArray(R.array.ticket_status_keys))));

        }
        if (getMaxListValue() == null) {
            setDefaultStringValue(sharedPreferences,R.string.SettingsMaxListItemsKey,context.getString(R.string.defaultValue_maxListItems));

        }
        if (getTicketSynchronization() == null) {
            setDefaultStringValue(sharedPreferences,R.string.SettingsSynchronizeTicketListKey,context.getString(R.string.defaultValue_synchronizeTicketList));

        }
        if (getTimeOutValue() == null){
            setDefaultStringValue(sharedPreferences,R.string.SettingsTimeOutKey,context.getString(R.string.defaultValue_timeOut));
            setDefaultBooleanValue(sharedPreferences,R.string.SettingsScreenLockKey,true);

        }
        if (getSizeOfDownloadedFile() == null){
            setDefaultStringValue(sharedPreferences,R.string.SettingsDownloadedFileSizeKey,context.getString(R.string.defaultValue_fileSize));

        }
        //getDate();  - press no

        //ha mas az uj URL mint ami mar a property file-ba szerepel => valtoztassa meg a property file -ba mentett url-t
        if (getWebUrl() == null || !(getWebUrl().equals(UIConstans.WEBURL_ADDFILE_SOAP))){
            setDefaultStringValue(sharedPreferences,R.string.sharedPreference_addFileWebUrl,UIConstans.WEBURL_ADDFILE_SOAP);
        }
      //  getWebUrl();

        if (getDateOfAppUpdate() == null){
            setDefaultStringValue(sharedPreferences,R.string.InfoAppDateofUpdateKey, EnvironmentTool.dateInString(new Date()));
        }

        if (getSizeOfApp() == null || !(getSizeOfApp().equals(EnvironmentTool.getAppSize()))){
            setDefaultStringValue(sharedPreferences,R.string.InfoAppSizeKey,EnvironmentTool.getAppSize());
        }
        //getDateOfAppUpdate(); -

        //System.out.println(" getSizeOfApp = "+getSizeOfApp()+"  --- aktualis app meret = "+EnvironmentTool.getAppSize());
    }

    /**
     *  - reset user settings
     *  - property file get default values
     */

    public void resetSettings(Context context, SharedPreferences sharedPreferences){
        setDefaultStringValue(sharedPreferences,R.string.SettingsLanguageKey, context.getString(R.string.defaultValue_language));
        setDefaultStringValue(sharedPreferences,R.string.SettingsMaxListItemsKey,context.getString(R.string.defaultValue_maxListItems));
        setDefaultStringValue(sharedPreferences,R.string.SettingsSynchronizeTicketListKey,context.getString(R.string.defaultValue_synchronizeTicketList));
        setDefaultStringValue(sharedPreferences,R.string.SettingsTimeOutKey,context.getString(R.string.defaultValue_timeOut));
        setDefaultStringValue(sharedPreferences,R.string.SettingsDownloadedFileSizeKey,context.getString(R.string.defaultValue_fileSize));
        setDefaultBooleanValue(sharedPreferences,R.string.SettingsScreenLockKey,true);
        setDefaultStringSetValue(sharedPreferences,R.string.SettingsStatusQueryKey,new HashSet<String>(Arrays.asList(context.getResources().getStringArray(R.array.ticket_status_keys))));


    }

    public void setDefaultStringValue( SharedPreferences sharedPreferences,int key, String value ){
        sharedPreferences.edit().putString(context.getString(key), value).commit();
    }

    public void setDefaultBooleanValue(SharedPreferences sharedPreferences, int key, boolean value){
        sharedPreferences.edit().putBoolean(context.getString(key),value).commit();
    }

    public void setDefaultStringSetValue(SharedPreferences sharedPreferences,int key,HashSet<String> value){
        sharedPreferences.edit().putStringSet(context.getString(key),value).commit();
    }

}
