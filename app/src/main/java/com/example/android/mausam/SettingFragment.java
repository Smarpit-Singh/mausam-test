package com.example.android.mausam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

public class SettingFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener{


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.pref_general);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        int count = preferenceScreen.getPreferenceCount();

        for (int i=0; i<count; i++){
            Preference p = preferenceScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(p.getKey(), "");
                setPrefernceSummary(p, value);
            }
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference preference = findPreference(key);

        if (preference != null){
            if (!(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(key, "");
                setPrefernceSummary(preference , value);
            }
        }
    }


    public void setPrefernceSummary(Preference prefernceSummary, Object object){
        String stringValue = object.toString();
        String key = prefernceSummary.getKey();

        if (prefernceSummary instanceof ListPreference){

            ListPreference listPreference = (ListPreference) prefernceSummary;
            int prefIndex = listPreference.findIndexOfValue(stringValue);

            if (prefIndex > 0){
                prefernceSummary.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }else {
            prefernceSummary.setSummary(stringValue);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onStop() {
        super.onStop();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
