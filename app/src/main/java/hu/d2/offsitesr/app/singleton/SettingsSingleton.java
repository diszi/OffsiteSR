package hu.d2.offsitesr.app.singleton;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;

/**
 * Created by csabinko on 2017.09.19..
 *
 * Settings task/menu
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

    /**
     *
     * @return - the selected language value
     */
    public String getLanguage(){
        return sharedPreferences.getString(context.getString(R.string.SettingsLanguageKey),null);
    }

    /**
     *
     * @return - a list with selected status
     */
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

    /**
     *
     * @return - the set maximum list value
     */
    public String getMaxListValue(){
        return sharedPreferences.getString(context.getString(R.string.SettingsMaxListItemsKey),null);
    }

    /**
     *
     * @return - the set ticket synchronize value
     */
   public String getTicketSynchronization() {
        return sharedPreferences.getString(context.getString(R.string.SettingsSynchronizeTicketListKey), null);
    }

    /**
     *
     * @return - the set time out value (min)
     */
    public String getTimeOutValue(){
        return sharedPreferences.getString(context.getString(R.string.SettingsTimeOutKey),null);
    }

    /**
     * @return - the set screen lock value (min)
     */
    public boolean getScreenLockValue(){
       return  sharedPreferences.getBoolean(context.getString(R.string.SettingsScreenLockKey),false);
    }

    /**
     * @return - the size of downloaded file
     */
    public String getSizeOfDownloadedFile(){
        return  sharedPreferences.getString(context.getString(R.string.SettingsDownloadedFileSizeKey),null);
    }

    /**
     * @return - the date when user pressed No button at update dialog
     */
    public String getDate(){
        return sharedPreferences.getString(context.getString(R.string.sharedPreference_getDate),null);
    }


    public String getWebUrl(){
        return sharedPreferences.getString(context.getString(R.string.sharedPreference_addFileWebUrl),null);
    }

    /**
     * @return - date of update
     */
    public String getDateOfAppUpdate(){
        return sharedPreferences.getString(context.getString(R.string.InfoAppDateofUpdateKey),null);
    }


    /**
     *
     * @return - the size of the application that is saved in the file
     */
    public String getSizeOfApp(){
        return sharedPreferences.getString(context.getString(R.string.InfoAppSizeKey),null);
    }


    /**
     * @param context - actual context of activity
     * @param userName - logged username
     *
     * Property file initialization with default values
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
        if (getWebUrl() == null || !(getWebUrl().equals(UIConstans.WEBURL_ADDFILE_SOAP))){
            setDefaultStringValue(sharedPreferences,R.string.sharedPreference_addFileWebUrl,UIConstans.WEBURL_ADDFILE_SOAP);
        }

        if (getDateOfAppUpdate() == null){
            setDefaultStringValue(sharedPreferences,R.string.InfoAppDateofUpdateKey, EnvironmentTool.convertDate(new Date(),UIConstans.DATE_PATTERN_HU));
        }

        if (getSizeOfApp() == null || !(getSizeOfApp().equals(EnvironmentTool.getAppSize()))){
            setDefaultStringValue(sharedPreferences,R.string.InfoAppSizeKey,EnvironmentTool.getAppSize());
        }

    }


    /**
     * @param context - actual context of activity where called this method
     * @param sharedPreferences - property file, where settings values was saved
     *
     * Reset user settings  - @param sharedPreferences will receive the default settings
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
