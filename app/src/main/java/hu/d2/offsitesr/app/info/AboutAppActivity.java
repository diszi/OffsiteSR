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



public class AboutAppActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TimerSingleton.getInstance().setMyActivity(this);
        TimerSingleton.getInstance().timerStart();
        EnvironmentTool.setLanguage(this, SettingsSingleton.getInstance().getLanguage());
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainAboutAppFragment()).commit();
        PreferenceManager.setDefaultValues(AboutAppActivity.this, R.xml.act_about_app_preferences,false);

    }

    /**
     *  - screen interaction - screen lock property
     */
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        TimerSingleton.getInstance().timerStop();
        TimerSingleton.getInstance().timerStart();
    }


    public static class MainAboutAppFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{


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
