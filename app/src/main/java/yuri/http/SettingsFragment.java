package yuri.http;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
{
    public static final String CALLBACK_URL = "callbackURL";

    public SettingsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // load preferences from XML
        addPreferencesFromResource(R.xml.preferences);

        // put the settings fragment on display
        //        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        initSummary();
        updatePrefsSummary(getPreferenceScreen().getSharedPreferences(), findPreference(CALLBACK_URL));
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key)
    {
        // change the preference
        //        if (key.equals(CALLBACK_URL))
        //        {
        //            Preference connPref = findPreference(key);
        //            //            connPref.setSummary(sharedPrefs.getString(key, "test"));
        //            //            updatePrefSummary(sharedPrefs, findPreference(key));
        //        }

        // Update summary
        updatePrefsSummary(sharedPreferences, findPreference(key));
    }

    protected void updatePrefsSummary(SharedPreferences sharedPreferences,
                            Preference pref)
    {
        if (pref == null)
        {
            return;
        }

        if (pref instanceof EditTextPreference)
        {
            // EditPreference
            EditTextPreference editTextPref = (EditTextPreference) pref;
            editTextPref.setSummary(editTextPref.getText());
        }
    }

    /*
     * Init summary
     */
    protected void initSummary()
    {
        int pcsCount = getPreferenceScreen().getPreferenceCount();

        for (int i = 0; i < pcsCount; i++)
        {
            initPrefsSummary(getPreferenceManager().getSharedPreferences(),
                    getPreferenceScreen().getPreference(i));
        }
    }

    /*
     * Init single Preference
     */
    protected void initPrefsSummary(SharedPreferences sharedPreferences,
                          Preference p)
    {
        if (p instanceof PreferenceCategory)
        {
            PreferenceCategory pCat = (PreferenceCategory) p;
            int pcCatCount = pCat.getPreferenceCount();

            for (int i = 0; i < pcCatCount; i++)
            {
                initPrefsSummary(sharedPreferences, pCat.getPreference(i));
            }
        }
    }
}