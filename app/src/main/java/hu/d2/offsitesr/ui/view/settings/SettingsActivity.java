package hu.d2.offsitesr.ui.view.settings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.app.singleton.TimerSingleton;
import hu.d2.offsitesr.util.EnvironmentTool;

import static hu.d2.offsitesr.R.string.SettingsMaxListItemsKey;

/**
 * Created by csabinko on 2017.09.18..
 *
 *  Settings menu -  this page uses act_settings_preferences.xml
 *  This activity shows one or more headers of preferences,
 *  each of which is associated with a PreferenceFragment
 *  to display the preferences of that header.
 */

public class   SettingsActivity extends PreferenceActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TimerSingleton.getInstance().setMyActivity(this);

        Log.e("---------------->", "SettingsActivity");
        EnvironmentTool.setLanguage(this,SettingsSingleton.getInstance().getLanguage());
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainSettingsFragment()).commit();
        PreferenceManager.setDefaultValues(SettingsActivity.this, R.xml.act_settings_preferences,false);

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

    /**
     * Interface definition for a callback to be invoked when a shared preference is changed.
     */
    public static class MainSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{


        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName(SettingsSingleton.getInstance().getFileConstant());
            addPreferencesFromResource(R.xml.act_settings_preferences);
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
         * @param sharedPreferences : property file for user, contains settings values
         *                           The SharedPreferences that received the change.
         * @param key - identification, the key of the preference that was changed, added, or removed.
         * Called when a shared preference is changed, added, or removed.
         * This may be called even if a preference is set to its existing value.
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //FIXME: It's ugly as hell


            if (key.equals(getString(R.string.SettingsMaxListItemsKey))) {
                setEditTextPreferenceDetails(key, R.string.SettingsMaxListItemsSummary);
            } else if (key.equals(getString(R.string.SettingsSynchronizeTicketListKey))) {
                setEditTextPreferenceDetails(key, R.string.SettingsSynchronizeTicketListSummary);
            } else if (key.equals(getString(R.string.SettingsMaxHistoryLengthKey))) {
                setEditTextPreferenceDetails(key, R.string.SettingsMaxHistoryLengthSummary);
            } else if (key.equals(getString(R.string.SettingsSynchronizeHistoryKey))) {
                setEditTextPreferenceDetails(key, R.string.SettingsSynchronizeHistorySummary);
            } else if (key.equals(getString(R.string.SettingsMaxLogSizeKey))) {
                setEditTextPreferenceDetails(key, R.string.SettingsMaxLogSizeSummary);
            } else if (key.equals(getString(R.string.SettingsMaxLogLevelKey))) {
                setListPreferenceDetails(key, R.string.SettingsMaxLogLevelSummary);
            } else if (key.equals(getString(R.string.SettingsLogRetentionKey))) {
                setEditTextPreferenceDetails(key, R.string.SettingsLogRetentionSummary);
            } else if (key.equals(getString(R.string.SettingsTimeOutKey))) {
                setEditTextPreferenceDetails(key, R.string.SettingsTimeOutSummary);
                String timeOut = sharedPreferences.getString(key, null);

            } else if (key.equals(getString(R.string.SettingsScreenLockKey))) {
                setSwitchPreferenceDetails(key, R.string.SettingsScreenLockSummary);
                boolean value = sharedPreferences.getBoolean(key, true);

                if (value == false) {
                    EnvironmentTool.setScreenLockOff(getActivity());
                } else {
                    EnvironmentTool.setScreenLockOn(getActivity());
                }

            } else if (key.equals(getString(R.string.SettingsLanguageKey))) {
                setListPreferenceDetails(key, R.string.SettingsLanguageSummary);
                String language = sharedPreferences.getString(key, null);
                EnvironmentTool.setLanguage(this.getActivity(), language);
                getActivity().finish();
                Intent intent = getCallingIntent(getActivity().getApplicationContext());
                startActivity(intent);

            } else if (key.equals(getString(R.string.SettingsStatusQueryKey))) {
                setMultiSelectListPreferenceDetails(key, R.string.SettingsStatusQuerySummary);
                getActivity().finish();
                Intent intent = getCallingIntent(getActivity().getApplicationContext());
                startActivity(intent);
            }
//            } else if (key.equals(getString(R.string.SettingsDownloadedFileSizeKey))) {
//                setEditTextPreferenceDetails(key, R.string.SettingsDownloadedFileSizeSummary);
//            }

        }

        /**
         *  - refresh Settings page
         */
         public static Intent getCallingIntent(Context context) {
            return new Intent(context, SettingsActivity.class);
        }


        private void setEditTextPreferenceDetails(String key, int summaryId) {
            EditTextPreference editTextPreference = (EditTextPreference) findPreference(key);
            String summaryValue = editTextPreference.getText();

            if ((key.equals(getString(R.string.SettingsMaxListItemsKey)) && (Integer.parseInt(summaryValue)  > 100 )) || (key.equals(getString(R.string.SettingsSynchronizeTicketListKey)) && (Integer.parseInt(summaryValue) > 360))){
                    Toast.makeText(getContext(),getString(R.string.settings_error_msg),Toast.LENGTH_SHORT).show();
                }else
                    {
                        editTextPreference.setSummary(getString(summaryId, editTextPreference.getText()));
                    }
        }

        private void setListPreferenceDetails(String key, int summaryId) {
            ListPreference listPreference = (ListPreference) findPreference(key);
            listPreference.setSummary(getString(summaryId, listPreference.getValue()));
        }

        private void setMultiSelectListPreferenceDetails(String key, int summaryId){
            MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) findPreference(key);
            multiSelectListPreference.setSummary(getString(summaryId,multiSelectListPreference.getEntries()));
        }

        private void setSwitchPreferenceDetails(String key, int summaryId){
            SwitchPreference switchPreference = (SwitchPreference) findPreference(key);
            switchPreference.setSummary(getString(summaryId,switchPreference.getSwitchTextOff()));
        }


        public void setupPreferences() {
            setEditTextPreferenceDetails(getString(SettingsMaxListItemsKey),R.string.SettingsMaxListItemsSummary);
            setEditTextPreferenceDetails(getString(R.string.SettingsSynchronizeTicketListKey),R.string.SettingsSynchronizeTicketListSummary);
            setEditTextPreferenceDetails(getString(R.string.SettingsMaxHistoryLengthKey),R.string.SettingsMaxHistoryLengthSummary);
            setEditTextPreferenceDetails(getString(R.string.SettingsSynchronizeHistoryKey),R.string.SettingsSynchronizeHistorySummary);
            setEditTextPreferenceDetails(getString(R.string.SettingsMaxLogSizeKey),R.string.SettingsMaxLogSizeSummary);
            setListPreferenceDetails(getString(R.string.SettingsMaxLogLevelKey),R.string.SettingsMaxLogLevelSummary);
            setEditTextPreferenceDetails(getString(R.string.SettingsLogRetentionKey),R.string.SettingsLogRetentionSummary);
            setEditTextPreferenceDetails(getString(R.string.SettingsTimeOutKey),R.string.SettingsTimeOutSummary);
            setSwitchPreferenceDetails(getString(R.string.SettingsScreenLockKey),R.string.SettingsScreenLockSummary);
            setMultiSelectListPreferenceDetails(getString(R.string.SettingsStatusQueryKey),R.string.SettingsStatusQuerySummary);
            setListPreferenceDetails(getString(R.string.SettingsLanguageKey),R.string.SettingsLanguageSummary);
            //setEditTextPreferenceDetails(getString(R.string.SettingsDownloadedFileSizeKey),R.string.SettingsDownloadedFileSizeSummary);
        }


    }


}
