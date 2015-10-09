package telematic.cl.popmovies.net;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import telematic.cl.popmovies.data.MovieContract;
import telematic.cl.popmovies.util.Movies;
import telematic.cl.popmovies.util.Reviews;

import static telematic.cl.popmovies.data.MovieContract.MovieEntry.COLUMN_REVIEWS;
import static telematic.cl.popmovies.data.MovieContract.MovieEntry.COLUMN_VIDEOS;
import static telematic.cl.popmovies.util.Consts.COL_MOVIE_KEY;
import static telematic.cl.popmovies.util.Consts.COL_REVIEWS;
import static telematic.cl.popmovies.util.Consts.COL_VIDEOS;


/**
 * Created by crised on 08-10-15.
 */
public class TestNet extends AndroidTestCase {

    public static final String LOG_TAG = TestNet.class.getSimpleName();

    private List<Movies.Result> mMovies;
    private String mReviewsJson;
    private String mTrailerJson;
    private Vector<ContentValues> mcVVector;


    public void testReviewFetch() {
        MovieServiceHelper serviceHelper = new MovieServiceHelper(mContext);
        String review = serviceHelper.fetchReviews(135397);
        //Log.d(LOG_TAG, serviceHelper.fetchReviews(135397));
        Type listType = new TypeToken<ArrayList<Reviews.Result>>() {
        }.getType();
        List<Reviews.Result> reviews = new Gson().fromJson(review, listType);
        // Log.d(LOG_TAG, reviews.toString());
    }

    public void testUpdateService() {

        deleteAllRecordsFromProvider();

        mMovies = new MovieServiceHelper(mContext).fetchMovies();
        if (mMovies == null) return; // no net
        fillCVVector();
        if (mcVVector.size() == 0) return;
        ContentValues[] cvArray = new ContentValues[mcVVector.size()];
        mcVVector.toArray(cvArray);
        Integer deletedRowsNonFavorite;
        //deletedRowsNonFavorite = getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, sMoviesFavorites, new String[]{"0"});
        //Log.d(LOG_TAG, "Deleted:  " + deletedRowsNonFavorite + " non favorites rows.");
        getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        Log.d(LOG_TAG, "Sync Complete. " + mcVVector.size() + " Inserted");


        //pick a MovieUri randomly = 9
        Uri movieUri = MovieContract.MovieEntry.buildMovieUri(1);

        Cursor cursor = getContext().getContentResolver().query(movieUri, null, null, null, null);
        cursor.moveToFirst();
        long movieKey = cursor.getLong(COL_MOVIE_KEY);

        Log.d(LOG_TAG, String.valueOf(movieKey));
        Log.d(LOG_TAG, "Reviews:" + cursor.getString(COL_REVIEWS));
        Log.d(LOG_TAG, "Videos:" + cursor.getString(COL_VIDEOS));
        cursor.close();

        MovieServiceHelper serviceHelper = new MovieServiceHelper(mContext);
        String reviews = serviceHelper.fetchReviews(movieKey);
        String videos = serviceHelper.fetchVideos(movieKey);
        assertFalse(reviews.isEmpty());
        assertFalse(videos.isEmpty());
        Log.d(LOG_TAG, "Reviews: " + reviews);
        Log.d(LOG_TAG, "Videos: " + videos);

        ContentValues updatedValues = new ContentValues();

        updatedValues.put(COLUMN_REVIEWS, reviews);
        updatedValues.put(COLUMN_VIDEOS, videos);
        int rowsUpdated = getContext().getContentResolver().update(movieUri, updatedValues, null, null);
        cursor = getContext().getContentResolver().query(movieUri, null, null, null, null);
        cursor.moveToFirst();


        Log.d(LOG_TAG, String.valueOf(rowsUpdated));
        Log.d(LOG_TAG, "Reviews from Updated Column:" + cursor.getString(COL_REVIEWS));
        Log.d(LOG_TAG, "Video from Updated Column:" + cursor.getString(COL_VIDEOS));

        cursor.close();


        /*
        Uri movieUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
        long locationRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + movieUri);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(COLUMN_REVIEWS, "{[revi...]}");
        updatedValues.put(COLUMN_VIDEOS, "{[vide...]}");*/


    }

    private void fillCVVector() {
        mcVVector = new Vector<>(mMovies.size());
        for (Movies.Result movie : mMovies) {
            ContentValues movieValues = new ContentValues();
            Integer movieId = movie.getId();
            if (movieId == null) break;
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_KEY, movie.getId());
            movieValues.put(MovieContract.MovieEntry.COLUMN_LANGUAGE, movie.getOriginalLanguage());
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, movie.getReleaseDate());
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, movie.getVoteAverage());
            movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
            movieValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, "0");
            mcVVector.add(movieValues);
        }
    }

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Weather table during delete", 0, cursor.getCount());
        cursor.close();
    }

}
