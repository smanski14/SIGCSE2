package com.example.owen.sigcsevolunteer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Austin on 10/22/2015.
 */
 
 //This settingsFragment calls on preferences.xml to start and store values for the Preferences screen.
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
