package telematic.cl.popmovies;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import telematic.cl.popmovies.util.Movies;
import telematic.cl.popmovies.util.Reviews;
import telematic.cl.popmovies.util.Videos;

import static telematic.cl.popmovies.util.Consts.COL_DATE;
import static telematic.cl.popmovies.util.Consts.COL_OVERVIEW;
import static telematic.cl.popmovies.util.Consts.COL_POSTER_PATH;
import static telematic.cl.popmovies.util.Consts.COL_REVIEWS;
import static telematic.cl.popmovies.util.Consts.COL_TITLE;
import static telematic.cl.popmovies.util.Consts.COL_VIDEOS;
import static telematic.cl.popmovies.util.Consts.COL_VOTE_AVG;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private static final int DETAIL_LOADER = 1;
    private Uri mUri;

    private Movies.Result mMovie;
    private List<Reviews.Result> mReviews;
    private List<Videos.Result> mVideos;

    private View mRootView;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private ImageView mImageView;
    private Button mButtonFavorite;

    private Typeface mFont;

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
        // Log.d(LOG_TAG, "In Detail Fragment");
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        mRootView = inflater.inflate(R.layout.fragment_detail_wide, container, false);

        /*
        mRootView.setBackgroundColor(Color.LTGRAY);
        mTitle = (TextView) mRootView.findViewById(R.id.detail_title);
        mPlot = (TextView) mRootView.findViewById(R.id.detail_plot);
        mRating = (TextView) mRootView.findViewById(R.id.detail_rating_date);
        mImageView = (ImageView) mRootView.findViewById(R.id.detail_view);
        mButtonFavorite = (Button) mRootView.findViewById(R.id.detail_button_favorite);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView.setLayoutParams(params);

        mFont = Typeface.createFromAsset(getActivity().getAssets(),
                "fontawesome-webfont.ttf");

        setFavoriteIcon();*/

        return mRootView;

    }

    private void setFavoriteIcon() {
        mButtonFavorite.setTypeface(mFont);

    }

    private void addVideoButtons() {

        Button button = new Button(getActivity());
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //button.setId();
        button.setText(getResources().getString(R.string.icon_video));
        button.setTypeface(mFont);

        ViewGroup ll = (ViewGroup) ((ViewGroup) mRootView).getChildAt(0);
        ll.addView(button);

    }


    private void fillUI() {
        mTitle.setText(mMovie.getTitle());
        Picasso.with(getContext()).load(mMovie.getPosterUri()).into(mImageView);
        if (mReviews != null)
            if (mReviews.get(0).getContent() != null)
                mPlot.setText(mReviews.get(0).getContent());
            else mPlot.setText("");
        else mPlot.setText("");
    }

    private void transformCursorData(Cursor data) {

        mMovie = new Movies().new Result();
        mMovie.setOverview(data.getString(COL_OVERVIEW));
        mMovie.setReleaseDate(data.getString(COL_DATE));
        mMovie.setPosterPath(data.getString(COL_POSTER_PATH));
        mMovie.setTitle(data.getString(COL_TITLE));
        mMovie.setVoteAverage(data.getDouble(COL_VOTE_AVG));


        try {
            String reviewJson = data.getString(COL_REVIEWS);
            Type listType = new TypeToken<ArrayList<Reviews.Result>>() {
            }.getType();
            if (reviewJson != null) {
                if (reviewJson.length() > 3)
                    mReviews = new Gson().fromJson(reviewJson, listType);
            } else Log.d(LOG_TAG, "No Reviews!");

        } catch (JsonParseException e) {
            Log.e(LOG_TAG, "Parse Exception");
        }

        try {
            String videosJson = data.getString(COL_VIDEOS);
            Type listType = new TypeToken<ArrayList<Reviews.Result>>() {
            }.getType();
            if (videosJson != null) {
                if (videosJson.length() > 3)
                    mVideos = new Gson().fromJson(videosJson, listType);
            } else Log.d(LOG_TAG, "No Videos!");

        } catch (JsonParseException e) {
            Log.e(LOG_TAG, "Parse Exception");
        }


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
        transformCursorData(data);
        fillUI();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }
}