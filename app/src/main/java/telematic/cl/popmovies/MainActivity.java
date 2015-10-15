package telematic.cl.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import telematic.cl.popmovies.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieSyncAdapter.initializeSyncAdapter(this);
        MovieSyncAdapter.syncImmediately(this);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_main);
        View detail_fragment_container_view = findViewById(R.id.detail_fragment_container);
        if(detail_fragment_container_view!=null) Log.d(LOG_TAG, detail_fragment_container_view.toString());
        else Log.d(LOG_TAG,"Can't find detail_fragment_container_view!!");
        if (findViewById(R.id.detail_fragment_container) != null) {
            Log.d(LOG_TAG, "Inside a tablet!");
            mTwoPane = true;
            //We have only one activity, we needs to inflate fragment.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container,
                                new DetailFragment(),
                                DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else mTwoPane = false;
    }


    @Override
    public void onItemSelected(Uri movieUri) {
        Log.d(LOG_TAG, "On main activity");
        if (mTwoPane) {
            Log.d(LOG_TAG, "On a tablet!");
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, movieUri);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Log.d(LOG_TAG, "On a phone!");
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
}
