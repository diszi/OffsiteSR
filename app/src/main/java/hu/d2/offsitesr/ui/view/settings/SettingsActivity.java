package hu.d2.offsitesr.ui.view.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.util.EnvironmentTool;

/**
 * Created by csabinko on 2017.09.18..
 */

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.act_settings_preferences);
        setupPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // FIXME: It's ugly as hell
        if(key.equals(getString(R.string.SettingsMaxListItemsKey))) {
            setEditTextPreferenceDetails(key, R.string.SettingsMaxListItemsSummary);
        } else if(key.equals(getString(R.string.SettingsMaxHistoryLengthKey))) {
            setEditTextPreferenceDetails(key, R.string.SettingsMaxHistoryLengthSummary);
        } else if(key.equals(getString(R.string.SettingsMaxLogMaxSizeKey))) {
            setEditTextPreferenceDetails(key, R.string.SettingsMaxLogMaxSizeSummary);
        } else if(key.equals(getString(R.string.SettingsLogRetentionKey))) {
            setEditTextPreferenceDetails(key, R.string.SettingsLogRetentionSummary);
        } else if(key.equals(getString(R.string.SettingsScreenLockKey))) {
            setEditTextPreferenceDetails(key, R.string.SettingsScreenLockSummary);
        } else if(key.equals(getString(R.string.SettingsManageUsersKey))) {
            setEditTextPreferenceDetails(key, R.string.SettingsManageUsersSummary);
        } else if(key.equals(getString(R.string.SettingsLanguageKey))) {
            setListPreferenceDetails(key, R.string.SettingsLanguageSummary);
            ListPreference listPreference = (ListPreference) findPreference(key);
            EnvironmentTool.changeLanguage(this, listPreference.getValue());

            /* Restart activity to reload string resources */
            finish();
            startActivity(getIntent());
        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    // TODO: Use templates
    private void setEditTextPreferenceDetails(String key, int summaryId) {
        EditTextPreference editTextPreference = (EditTextPreference) findPreference(key);
        editTextPreference.setSummary(getString(summaryId, editTextPreference.getText()));
    }

    private void setListPreferenceDetails(String key, int summaryId) {
        ListPreference listPreference = (ListPreference) findPreference(key);
        // TODO: getEntry?
        listPreference.setSummary(getString(summaryId, listPreference.getValue()));
    }

    private void setEditTextPreferenceDetails(int keyId, int summaryId) {
        setEditTextPreferenceDetails(getString(keyId), summaryId);
    }

    private void setListPreferenceDetails(int keyId, int summaryId) {
        setListPreferenceDetails(getString(keyId), summaryId);
    }

    public void setupPreferences() {
        setEditTextPreferenceDetails(R.string.SettingsMaxListItemsKey, R.string.SettingsMaxListItemsSummary);
        setEditTextPreferenceDetails(R.string.SettingsMaxHistoryLengthKey, R.string.SettingsMaxHistoryLengthSummary);
        setEditTextPreferenceDetails(R.string.SettingsMaxLogMaxSizeKey, R.string.SettingsMaxLogMaxSizeSummary);
        setEditTextPreferenceDetails(R.string.SettingsLogRetentionKey, R.string.SettingsLogRetentionSummary);
        setEditTextPreferenceDetails(R.string.SettingsScreenLockKey, R.string.SettingsScreenLockSummary);
        setEditTextPreferenceDetails(R.string.SettingsManageUsersKey, R.string.SettingsManageUsersSummary);
        setListPreferenceDetails(R.string.SettingsLanguageKey, R.string.SettingsLanguageSummary);
    }

}
