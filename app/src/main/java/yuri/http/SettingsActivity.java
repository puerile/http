package yuri.http;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceCategory;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
    public static final String CALLBACK_URL = "callbackURL";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // put the settings fragment on display
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    //    @Deprecated
    @Override
    protected void onResume()
    {
        super.onResume();
//        initSummary();
//      //  updatePrefSummary(getPreferenceManager().getSharedPreferences(),findPreference(CALLBACK_URL));
        //        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    //    @Deprecated
    @Override
    protected void onPause()
    {
        super.onPause();
        //        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Deprecated
    public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key)
    {
//        if (key.equals(CALLBACK_URL))
//        {
//            Preference connPref = findPreference(key);
////            connPref.setSummary(sharedPrefs.getString(key, "test"));
//            updatePrefSummary(sharedPrefs, findPreference(key));
//        }
    }

//    protected void initSummary()
//    {
//        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++)
//        {
//            initPrefSummary(getPreferenceManager().getSharedPreferences(), getPreferenceScreen().getPreference(i));
//        }
//    }

//    protected void initPrefSummary(SharedPreferences sharedPrefs, Preference pref)
//    {
//        if (pref instanceof PreferenceCategory)
//        {
//            PreferenceCategory prefCat = (PreferenceCategory) pref;
//            for (int i = 0; i < prefCat.getPreferenceCount(); i++)
//            {
//                initPrefSummary(sharedPrefs, prefCat.getPreference(i));
//            }
//        }
//
//        else
//        {
//            updatePrefSummary(sharedPrefs, pref);
//        }
//    }

    // update the summary [secondary text] of a setting
    protected void updatePrefSummary(SharedPreferences sharedPrefs, Preference pref)
    {
        if (pref != null /*&& pref instanceof EditTextPreference*/)
        {
            EditTextPreference editTextPref = (EditTextPreference) pref;
            editTextPref.setSummary(editTextPref.getText());
        }
    }
}
