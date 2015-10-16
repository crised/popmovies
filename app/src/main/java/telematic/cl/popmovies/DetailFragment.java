package telematic.cl.popmovies;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static telematic.cl.popmovies.util.Consts.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private static final int DETAIL_LOADER = 1;
    private Uri mUri;

    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private ImageView mImageView;


    public DetailFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "In Detail Fragment");
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View root = container;
        root.setBackgroundColor(Color.LTGRAY);
        mTitle = (TextView) root.findViewById(R.id.detail_title);
        mPlot = (TextView) root.findViewById(R.id.detail_plot);
        mRating = (TextView) root.findViewById(R.id.detail_rating_date);
        mImageView = (ImageView) root.findViewById(R.id.detail_view);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        root.setLayoutParams(params);


        return null;


        //    Picasso.with(getContext()).load(intent.getStringExtra("uri")).into(imageView);
        //    title.setText(intent.getStringExtra("title"));
        //  plot.setText(intent.getStringExtra("plot"));
        //rating.setText("User Rating: " + intent.getStringExtra("rating") + "   "
        //      + "Release Date: " + intent.getStringExtra("date"));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    null,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) return;
        mTitle.setText(data.getString(COL_TITLE));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }
}