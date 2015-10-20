package telematic.cl.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import telematic.cl.popmovies.data.MovieContract;
import telematic.cl.popmovies.sync.DetailUpdateService;
import telematic.cl.popmovies.util.Movies;

import static telematic.cl.popmovies.util.Consts.COL_DATE;
import static telematic.cl.popmovies.util.Consts.COL_FAVORITE;
import static telematic.cl.popmovies.util.Consts.COL_LANGUAGE;
import static telematic.cl.popmovies.util.Consts.COL_MOVIE_ID;
import static telematic.cl.popmovies.util.Consts.COL_MOVIE_KEY;
import static telematic.cl.popmovies.util.Consts.COL_OVERVIEW;
import static telematic.cl.popmovies.util.Consts.COL_POPULARITY;
import static telematic.cl.popmovies.util.Consts.COL_POSTER_PATH;
import static telematic.cl.popmovies.util.Consts.COL_TITLE;
import static telematic.cl.popmovies.util.Consts.COL_VOTE_AVG;
import static telematic.cl.popmovies.util.Consts.MOVIE_ID;
import static telematic.cl.popmovies.util.Consts.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private static final int MOVIES_LOADER = 0;

    static final String MAIN_SORT_URI = "MAIN_SORT_URI";


    private ImageAdapter mAdapter;

    private int mSort; // 0 Most Popular, 1 Highest Ranked, 2 Favorites.
    private List<Movies.Result> mMovieList;
    private Cursor mCursorData;

    private static final String[] MOVIES_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_KEY,
            MovieContract.MovieEntry.COLUMN_LANGUAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVG,
            MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_FAVORITE
            //in this fragment, there is no need for Videos & Reviews
    };


    public MainFragment() {
    }

    public interface Callback {
        public void onItemSelected(Uri movieUri);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mSort = arguments.getInt(MainFragment.MAIN_SORT_URI);
            Log.d(LOG_TAG, String.valueOf(mSort));
        }


        mAdapter = new ImageAdapter(getContext());
        GridView gridView = (GridView) inflater.inflate(R.layout.fragment_main, container, false);
        gridView.setAdapter(mAdapter);
        LinearLayout.LayoutParams gParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                this.getResources().getInteger(R.integer.columns)
                        * this.getResources().getInteger(R.integer.column_width),
                ViewGroup.LayoutParams.WRAP_CONTENT));
        gridView.setLayoutParams(gParams);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Uri movieUri = MovieContract.MovieEntry.
                        buildMovieUri(mMovieList.get(position).get_id());

                //Start DetailUpdateServie, no matter what.
                Intent serviceIntent = new Intent(getContext(), DetailUpdateService.class);
                serviceIntent.putExtra(MOVIE_ID, movieUri);
                getActivity().startService(serviceIntent);

                //Let parent Activity, either update fragment or
                Callback callbackActivity = (Callback) getActivity();
                callbackActivity.onItemSelected(movieUri);
            }
        });
        return gridView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (mSort) {
            case SORT_MOST_POPULAR:
                Log.d(LOG_TAG, "Loading Most Popular");
                return new CursorLoader(getActivity(),
                        MovieContract.MovieEntry.CONTENT_URI,
                        MOVIES_COLUMNS,
                        null,
                        null,
                        MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC");
            case SORT_HIGH_RATED:
                Log.d(LOG_TAG, "Loading Highest Rating");
                return new CursorLoader(getActivity(),
                        MovieContract.MovieEntry.CONTENT_URI,
                        MOVIES_COLUMNS,
                        null,
                        null,
                        MovieContract.MovieEntry.COLUMN_VOTE_AVG + " DESC");
            case SORT_FAVORITES:
                return new CursorLoader(getActivity(),
                        MovieContract.MovieEntry.buildFavMovieUri(),
                        MOVIES_COLUMNS,
                        null,
                        null,
                        null);
            default:
                Log.e(LOG_TAG, "Need criteria to load");
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) return;
        mCursorData = data;
        setListFromCursor();
        Log.d(LOG_TAG, "# Movies: " + String.valueOf(mMovieList.size()));
        mAdapter.getMovies().clear();
        mAdapter.notifyDataSetChanged();
        mAdapter.getMovies().addAll(mMovieList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.getMovies().clear();
        loader = null;
    }

    private void setListFromCursor() {
        mMovieList = new ArrayList<>();
        for (mCursorData.moveToFirst(); !mCursorData.isAfterLast(); mCursorData.moveToNext()) {
            Movies movies = new Movies();
            Movies.Result movie = movies.new Result();
            movie.set_id(mCursorData.getInt(COL_MOVIE_ID));
            movie.setId(mCursorData.getInt(COL_MOVIE_KEY)); // not confuse _ID with movieKey=movieid
            movie.setOriginalLanguage(mCursorData.getString(COL_LANGUAGE));
            movie.setOverview(mCursorData.getString(COL_OVERVIEW));
            movie.setReleaseDate(mCursorData.getString(COL_DATE));
            movie.setPosterPath(mCursorData.getString(COL_POSTER_PATH));
            movie.setPopularity(mCursorData.getDouble(COL_POPULARITY));
            movie.setTitle(mCursorData.getString(COL_TITLE));
            movie.setVoteAverage(mCursorData.getDouble(COL_VOTE_AVG));
            movie.setVoteCount(mCursorData.getInt(COL_VOTE_AVG));
            movie.setFavorite(mCursorData.getInt(COL_FAVORITE));
            mMovieList.add(movie);
        }
    }

}