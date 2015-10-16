package telematic.cl.popmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import telematic.cl.popmovies.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String MOVIESFRAGMENT_TAG = "MFTAG";
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieSyncAdapter.initializeSyncAdapter(this);
        MovieSyncAdapter.syncImmediately(this);
        Log.d(LOG_TAG, "onCreate, before setContentView");
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate, after setContentView");
        if (findViewById(R.id.sw600dp) != null) {
            Log.d(LOG_TAG, "Inside a tablet!");
            mTwoPane = true;
            //We have only one activity, we needs to inflate fragment.
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            //Root View of the activity - LinearLayout in this case.
            final ViewGroup rootView = (ViewGroup) ((ViewGroup) this
                    .findViewById(android.R.id.content)).getChildAt(0);

            //Inflate both fragments, Add them to Child Views.
            rootView.addView(inflater.inflate(R.layout.fragment_main, null, false));
            //  rootView.addView(inflater.inflate(R.layout.fragment_detail, null));
            View detailView = inflater.inflate(R.layout.fragment_detail_wide, null, false);
            detailView.setBackgroundColor(Color.RED);

            rootView.addView(detailView);


            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(this);
            tv.setText("TRIALSSS");
            tv.setLayoutParams(lparams);
            tv.setBackgroundColor(Color.MAGENTA);
            rootView.addView(tv);


            //add fragment to
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.gridview_fragment_container,
                            new MoviesFragment(),
                            MOVIESFRAGMENT_TAG)
                    .commit();


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container,
                            new DetailFragment(),
                            DETAILFRAGMENT_TAG)
                    .commit();

        } else mTwoPane = false;
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
                    .replace(R.id.detail_fragment_container, fragment, DETAILFRAGMENT_TAG)
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
}
