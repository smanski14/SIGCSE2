package com.example.owen.sigcsevolunteer;

import android.os.Bundle;

/** This class provides the options page of the application.
 * @author Owen Galvin, 10/19/2015
 */
public class OptionActivity extends TaskActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
