package yuri.http;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.util.Log;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
{
    public static final String THEME = "theme";

    public SettingsFragment()
    {
        // required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // load preferences from XML
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        // initialise the pref summaries, so the values will be displayed
        initSummary();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key)
    {
        // update summary
        updatePrefsSummary(sharedPreferences, findPreference(key));
    }

    /*
     * init summary
     */
    protected void initSummary()
    {
        int pcsCount = getPreferenceScreen().getPreferenceCount();

        for (int i = 0; i < pcsCount; i++)
        {
            updatePrefsSummary(getPreferenceScreen().getSharedPreferences(),
                    getPreferenceScreen().getPreference(i));
        }
    }

    /*
     * update summary after a change
     */
    protected void updatePrefsSummary(SharedPreferences sharedPreferences,
                                      Preference pref)
    {
        // check if we have a preference
        if (pref == null)
        {
            return;
        }

        // only do this if it's a text preference
        // [there are no other preferences, so this is the only possibility]
        else if (pref instanceof EditTextPreference)
        {
            // EditPreference
            EditTextPreference editTextPref = (EditTextPreference) pref;
            editTextPref.setSummary(editTextPref.getText());
        }

        else if (pref instanceof ListPreference)
        {
            // ListPreference
            //            ListPreference listPref = (ListPreference) pref;
            //            listPref.setSummary(listPref.getEntry());
        }
    }
}