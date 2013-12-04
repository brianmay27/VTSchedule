/*
 * VT Schedulator
 * 12/11/2013
 */

package edu.vt.ece4564.vtschedulator;

import android.preference.PreferenceFragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

//-------------------------------------------------------------------------
/**
 * Settings Activity
 * Allows the user to set their Username, Password, and Major.
 */
public class SettingsActivity extends PreferenceActivity
implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        Settings settings = new Settings();
        this.addPreferencesFromResource(R.xml.main_pref);
        PreferenceManager.setDefaultValues(this, R.xml.main_pref, false);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
            settings).commit();
    }
    @Override
    public void onSharedPreferenceChanged(
        SharedPreferences sharedPreferences,
        String key)
    {
        // TODO Auto-generated method stub

    }
    public static class Settings extends PreferenceFragment
    {
        public void onCreate(Bundle saved) {
            super.onCreate(saved);
            android.util.Log.d("settings","In the fragment");
                addPreferencesFromResource(R.xml.main_pref);
        }
    }
}