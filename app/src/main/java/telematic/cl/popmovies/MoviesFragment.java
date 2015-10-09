package telematic.cl.popmovies;

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
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import telematic.cl.popmovies.data.MovieContract;
import telematic.cl.popmovies.util.Movies;

import static telematic.cl.popmovies.util.Consts.COL_DATE;
import static telematic.cl.popmovies.util.Consts.COL_FAVORITE;
import static telematic.cl.popmovies.util.Consts.COL_LANGUAGE;
import static telematic.cl.popmovies.util.Consts.COL_MOVIE_KEY;
import static telematic.cl.popmovies.util.Consts.COL_OVERVIEW;
import static telematic.cl.popmovies.util.Consts.COL_POPULARITY;
import static telematic.cl.popmovies.util.Consts.COL_POSTER_PATH;
import static telematic.cl.popmovies.util.Consts.COL_TITLE;
import static telematic.cl.popmovies.util.Consts.COL_VOTE_AVG;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private static final int MOVIES_LOADER = 0;

    private ImageAdapter mAdapter;

    private List<Movies.Result> mMovies;
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


    public MoviesFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        syncNet();
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void syncNet() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String preference = prefs.getString(getString(R.string.settings_key),
                getString(R.string.settings_default));
        // new NetTask().execute(preference);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAdapter = new ImageAdapter(getContext());


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mAdapter);

        /*
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                MovieOld movie = mAdapter.getMovies().get(position);
                Intent intent = new Intent(getActivity(), detail.class)
                        .putExtra("title", movie.getTitle())
                        .putExtra("uri", movie.getUri().toString())
                        .putExtra("plot", movie.getPlot())
                        .putExtra("rating", movie.getRating())
                        .putExtra("date", movie.getDate());
                startActivity(intent);
            }
        });*/


        return gridview;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d(LOG_TAG, "Loader created");
        Uri moviesUri = MovieContract.MovieEntry.CONTENT_URI;
        String sortOrder = MovieContract.MovieEntry._ID + " ASC";

        return new CursorLoader(getActivity(),
                moviesUri,
                MOVIES_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorData = data;
        setListFromCursor();
        mAdapter.getMovies().clear();
        mAdapter.getMovies().addAll(mMovies);

        /*for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(data.get(WHATEVER_COLUMN_INDEX_YOU_WANT));
        }


        mAdapter.getMovies().clear();
        mAdapter.getMovies().addAll(movies);
        mAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;

    }

    private void setListFromCursor() {

        mMovies = new ArrayList<>();
        for (mCursorData.moveToFirst(); !mCursorData.isAfterLast(); mCursorData.moveToNext()) {
            Movies movies = new Movies();
            Movies.Result movie = movies.new Result();
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
            mMovies.add(movie);
        }


    }


}