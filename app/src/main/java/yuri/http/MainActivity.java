package yuri.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity
{
    private static final String DEBUG_TAG = "HTTP!";
    private static final String ID_CALLBACK_URL = "callbackURL";
    private String callbackURL = "";
    private SharedPreferences.OnSharedPreferenceChangeListener listener = new PreferenceChangeListener();

    // standard methods

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // make home button usable
        // [this was used in compatibility mode]
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // register settings listener
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        prefs.registerOnSharedPreferenceChangeListener(listener);

        // read callbackURL from settings
        callbackURL = prefs.getString(SettingsFragment.CALLBACK_URL, ID_CALLBACK_URL);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // unregister settings listener
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }


    // action bar stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
        //        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // if settings are clicked
        if (id == R.id.action_settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // action listener

    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
        {
            // set callbackURL
            callbackURL = prefs.getString(SettingsFragment.CALLBACK_URL, ID_CALLBACK_URL);
        }
    }


    // functionality stuff

    // check if there's a network connection
    private boolean checkConnection()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    // start an HTTP call when the button is pressed
    public void httpCall(View view)
    {
        // first check if there's a connection to the internet
        if (checkConnection())
        {
            // check if there's a callback URL set
            if (callbackURL != null && !callbackURL.equals(""))
            {
                // now make the HTTP call using the URL specified in the settings
                new httpCallTask().execute(callbackURL);
            }

            // if there's no callback URL set, ask user to do this first
            else
            {
                toast(getResources().getString(R.string.noCallbackURL));
            }
        }

        // if there's no network connection, display a warning
        else
        {
            toast(getResources().getString(R.string.disconnected));
        }
    }

    // make the actual HTTP request
    private class httpCallTask extends AsyncTask<String, Void, String>
    {
        // save the response, so it can be displayed as a toast later
        String response;

        // start a new thread for the connection,
        // in case network is slow and it takes a while
        @Override
        protected String doInBackground(String... url)
        {
            try
            {
                // try to make the call
                // url[0] is the URL
                HttpURLConnection conn = (HttpURLConnection) new URL(url[0]).openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.connect();

                // write the HTTP response into the debug log
                Log.d(DEBUG_TAG, "response: " + conn.getResponseCode());

                response = conn.getResponseMessage();
                return conn.getResponseMessage();
            }

            catch (Exception e)
            {
                // write stuff in the debug log and save the error message
                Log.e(DEBUG_TAG, e.toString());
                response = e.getMessage();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            // display whatever the result of the HTTP request was as a toast
            // [or the error message if there was an exception,
            // which is hopefully never the case]
            toast(response);
        }
    }

    // show [system] messages as pop-ups
    private void toast(String msg)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG; // we want some time to actually read the message if it's not simply "OK"
        Toast toast;

        // if something went wrong just display the error message
        if (!msg.equals("OK"))
        {
            toast = Toast.makeText(context, msg, duration);
        }

        // otherwise, display which host was queried
        else
        {
            toast = Toast.makeText(context, callbackURL + ": " + msg, duration);
        }

        toast.show();
    }
}