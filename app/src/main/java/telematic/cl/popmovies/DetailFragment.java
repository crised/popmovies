package telematic.cl.popmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import static telematic.cl.popmovies.util.Consts.COL_FAVORITE;
import static telematic.cl.popmovies.util.Consts.COL_OVERVIEW;
import static telematic.cl.popmovies.util.Consts.COL_POSTER_PATH;
import static telematic.cl.popmovies.util.Consts.COL_REVIEWS;
import static telematic.cl.popmovies.util.Consts.COL_TITLE;
import static telematic.cl.popmovies.util.Consts.COL_VIDEOS;
import static telematic.cl.popmovies.util.Consts.COL_VOTE_AVG;

import static telematic.cl.popmovies.data.MovieContract.MovieEntry.COLUMN_FAVORITE;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "DETAIL_URI";

    private static final int DETAIL_LOADER = 1;
    private Uri mUri;
    private ShareActionProvider mShareActionProvider;


    private Movies.Result mMovie;
    private List<Reviews.Result> mReviews;
    private List<Videos.Result> mVideos;

    private LinearLayout mll;
    private TextView mTitle;
    private ImageView mImageView;
    private TextView mDate;
    private TextView mVote;
    private Button mButtonFavorite;
    private TextView mPlot;
    private LinearLayout mTrailersll;
    private LinearLayout mReviewsll;


    private Typeface mFont;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mVideos != null)
            mShareActionProvider.setShareIntent(createShareIntent());

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

        mll = (LinearLayout) inflater.inflate(R.layout.fragment_detail_wide,
                container, false);
        mTitle = (TextView) mll.findViewById(R.id.detail_title);
        mImageView = (ImageView) mll.findViewById(R.id.detail_view);
        mDate = (TextView) mll.findViewById(R.id.detail_date);
        mVote = (TextView) mll.findViewById(R.id.detail_vote);
        mPlot = (TextView) mll.findViewById(R.id.detail_plot);
        mButtonFavorite = (Button) mll.findViewById(R.id.detail_button_favorite);

        mTrailersll = (LinearLayout) mll.findViewById(R.id.detail_trailers_ll);
        mReviewsll = (LinearLayout) mll.findViewById(R.id.detail_reviews_ll);


        mFont = Typeface.createFromAsset(getActivity().getAssets(),
                "fontawesome-webfont.ttf");


        setFavoriteIcon();
        addVideoButtons();

        return mll;

    }

    private void setFavoriteIcon() {
        mButtonFavorite.setTypeface(mFont);
        mButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteStar();
            }
        });
    }

    private void addVideoButtons() {

    }

    private void favoriteStar() {
        ContentValues cv = new ContentValues();
        int toggle;
        if (mMovie.getFavorite() == 1) toggle = 0;
        else toggle = 1;
        cv.put(COLUMN_FAVORITE, toggle);
        getContext().getContentResolver().
                update(mUri,
                        cv,
                        null,
                        null);
        //Toast could be added here.
    }


    private void fillUI() {
        //add logic in order not to set twice values, loader get's called twice.
        Picasso.with(getContext()).load(mMovie.getPosterUri()).into(mImageView);
        mTitle.setText(mMovie.getTitle());
        mDate.setText(mMovie.getReleaseDate().trim().substring(0, 4));
        if (mMovie.getVoteAverage() != null)
            mVote.setText(String.valueOf(mMovie.getVoteAverage()) + "/10");
        mPlot.setText(mMovie.getOverview());

        if (!(mReviews == null || mReviews.isEmpty())) {
            for (Reviews.Result review : mReviews) {
                TextView textView =
                        new TextView(getContext());
                textView.setLayoutParams(new
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(getResources().getString(R.string.icon_video)
                        + "\n" + review.getContent());
                textView.setTypeface(mFont);
                textView.setPadding(2, 2, 2, 2);
                //textView.setTextAppearance(R.s);
                mReviewsll.addView(textView);

            }
        }

        if (!(mVideos == null || mVideos.isEmpty())) {
            for (final Videos.Result video : mVideos) {
                Button button = new Button(getActivity());
                button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setText(getResources().getString(R.string.icon_youtube));
                button.setTypeface(mFont);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mVideos != null) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(video.getUri())));
                        }
                    }
                });
                button.setPadding(2, 2, 2, 2);
                mTrailersll.addView(button);
            }
        }


    }

    private void transformCursorData(Cursor data) {

        mMovie = new Movies().new Result();
        mMovie.setOverview(data.getString(COL_OVERVIEW));
        mMovie.setReleaseDate(data.getString(COL_DATE));
        mMovie.setPosterPath(data.getString(COL_POSTER_PATH));
        mMovie.setTitle(data.getString(COL_TITLE));
        mMovie.setVoteAverage(data.getDouble(COL_VOTE_AVG));
        if (data.getInt(COL_FAVORITE) == 1) {
            mMovie.setFavorite(1);
            Log.d(LOG_TAG, "Movie is Favorite!");
        } else mMovie.setFavorite(0);


        try {
            String reviewJson = data.getString(COL_REVIEWS);
            Type listType = new TypeToken<ArrayList<Reviews.Result>>() {
            }.getType();
            if (reviewJson != null) {
                if (reviewJson.length() > 3)
                    mReviews = new Gson().fromJson(reviewJson, listType);
            } else mReviews = null;

        } catch (JsonParseException e) {
            Log.e(LOG_TAG, "Parse Exception");
        }

        try {
            String videosJson = data.getString(COL_VIDEOS);
            Type listType = new TypeToken<ArrayList<Videos.Result>>() {
            }.getType();
            if (videosJson != null) {
                if (videosJson.length() > 3)
                    mVideos = new Gson().fromJson(videosJson, listType);
            } else mVideos = null;

        } catch (JsonParseException e) {
            Log.e(LOG_TAG, "Parse Exception");
        }


    }

    private Intent createShareIntent() {
        if (mVideos == null || mVideos.isEmpty()) return null;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        if (mVideos.get(0).getUri() != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "See this great Movie! " +
                            mVideos.get(0).getUri());
        }
        return shareIntent;
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
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }
}