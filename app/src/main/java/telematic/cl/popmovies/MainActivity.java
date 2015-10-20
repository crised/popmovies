package telematic.cl.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import telematic.cl.popmovies.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";
    private static final String MOVIES_FRAGMENT_TAG = "MFTAG";
    protected boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieSyncAdapter.initializeSyncAdapter(this);
        if (savedInstanceState == null)
            MovieSyncAdapter.syncImmediately(this); //Avoid SyncAdapter initialize twice on orientation change
        setContentView(R.layout.activity_main);// The resource will be inflated, adding all top-level views to the activity.
        if (findViewById(R.id.sw600dp) != null) {
            Log.d(LOG_TAG, "Inside a tablet!");
            mTwoPane = true;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container,
                            fragmentUponSettings(),
                            MOVIES_FRAGMENT_TAG)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container,
                            fragmentUponSettings(),
                            MOVIES_FRAGMENT_TAG)
                    .commit();
            mTwoPane = false;
        }
    }


    @Override
    public void onItemSelected(Uri movieUri) {
        Log.d(LOG_TAG, "On main activity");
        if (mTwoPane) {
            Log.d(LOG_TAG, "onItemSelected: On a tablet!");
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, movieUri);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Log.d(LOG_TAG, "onItemSelected: On a phone!");
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(movieUri);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Fragment fragmentUponSettings() {
        Fragment mainFragment = new MainFragment();
        Bundle args = new Bundle();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String preference = prefs.getString(getString(R.string.settings_key),
                getString(R.string.settings_default));
        args.putInt(MainFragment.MAIN_SORT_URI, Integer.valueOf(preference));
        args.putBoolean(MainFragment.TWO_PANE_URI, mTwoPane);
        mainFragment.setArguments(args);
        return mainFragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container,
                        fragmentUponSettings(),
                        MOVIES_FRAGMENT_TAG)
                .commit();
    }


}
