package hu.d2.offsitesr.app.info;


import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import android.os.Bundle;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.app.singleton.TimerSingleton;
import hu.d2.offsitesr.util.EnvironmentTool;

/**
 *  About menu contains general informations about application. This activity load About xml.
 */

public class AboutAppActivity extends PreferenceActivity {


    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     * @param savedInstanceState - If the activity is being re-initialized after previously being
     *                             shut down then this Bundle contains the data
     *                             it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TimerSingleton.getInstance().setMyActivity(this);
        EnvironmentTool.setLanguage(this, SettingsSingleton.getInstance().getLanguage());
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainAboutAppFragment()).commit();
        PreferenceManager.setDefaultValues(AboutAppActivity.this, R.xml.act_about_app_preferences,false);

    }

    /**
     * Called whenever a key, touch, or trackball event is dispatched to the activity.
     *  - user interaction on screen - reset timer (stop timer + start timer)
     */
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        TimerSingleton.getInstance().resetTimer();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }


    public static class MainAboutAppFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{


        /**
         * Called to do initial creation of a fragment.
         * @param savedInstanceState - If the fragment is being re-created from a
         *                             previous saved state, this is the state.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName(SettingsSingleton.getInstance().getFileConstant());
            addPreferencesFromResource(R.xml.act_about_app_preferences);
            setupPreferences();
        }

        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }


        /**
         *
         * @param key - points to the fields name on the About page
         * @param summaryId - points to the fields summary on the About page
         *
         *  This method sets the values of the different fields with the general information about the application
         */
        private void setPreferenceDetails(String key,int summaryId){
            Preference preference = (Preference) findPreference(key);
            if (key.equals(getString(R.string.InfoAppVersionKey))){
                preference.setSummary(getString(summaryId,EnvironmentTool.getVersionApp()));
            }
            else
                if (key.equals(getString(R.string.InfoAppDateofUpdateKey))){
                    preference.setSummary(getString(summaryId,getPreferenceScreen().getSharedPreferences().getString(key,null)));
                }
                else
                    if (key.equals(getString(R.string.InfoAppSizeKey))){
                        preference.setSummary(getString(summaryId,getPreferenceScreen().getSharedPreferences().getString(key,null)));
                    }else
                    if (key.equals(getString(R.string.InfoAppNameKey)))
                    {
                        preference.setSummary(getString(summaryId,getString(R.string.app_name)));
                    }
        }


        public void setupPreferences() {
            setPreferenceDetails(getString(R.string.InfoAppVersionKey),R.string.InfoAppVersionSummary);
            setPreferenceDetails(getString(R.string.InfoAppDateofUpdateKey),R.string.InfoAppDateofUpdateSummary);
            setPreferenceDetails(getString(R.string.InfoAppSizeKey),R.string.InfoAppSizeSummary);
            setPreferenceDetails(getString(R.string.InfoAppNameKey),R.string.InfoAppNameSummary);

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        }
    }


}
