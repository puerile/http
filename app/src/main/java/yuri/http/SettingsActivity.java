package yuri.http;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity
{
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
}
