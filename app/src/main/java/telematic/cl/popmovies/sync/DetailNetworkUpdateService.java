package telematic.cl.popmovies.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import telematic.cl.popmovies.net.MovieServiceHelper;

import static telematic.cl.popmovies.util.Consts.*;
import static telematic.cl.popmovies.data.MovieContract.MovieEntry.*;

/**
 * Created by crised on 08-10-15.
 */
public class DetailNetworkUpdateService extends IntentService {

    public final String LOG_TAG = DetailNetworkUpdateService.class.getSimpleName();

    public DetailNetworkUpdateService() {
        super("DetailNetworkUpdateService");
    }

    //uri of selected movie
    //fetchReviews, fetchVideos, *update* them.
    @Override
    protected void onHandleIntent(Intent intent) {
        Uri movieUri = intent.getParcelableExtra(MOVIE_ID);
        if (movieUri == null) return;
        Cursor cursor = getContentResolver().query(movieUri, null, null, null, null);
        if (!cursor.moveToFirst()) return;
        long movieKey = cursor.getLong(COL_MOVIE_KEY);
        cursor.close();
        if (movieKey == 0) return;
        MovieServiceHelper serviceHelper = new MovieServiceHelper(this);
        String reviews = serviceHelper.fetchReviews(movieKey);
        String videos = serviceHelper.fetchVideos(movieKey);
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(COLUMN_REVIEWS, reviews);
        updatedValues.put(COLUMN_VIDEOS, videos);
        int rowUpdated = getContentResolver().update(movieUri, updatedValues, null, null); //movie/#
        if (rowUpdated != 1) Log.e(LOG_TAG, "Couldn't update reviews & videos!");
        //testCode
        Cursor updatedCursor = getContentResolver().query(movieUri, null, null, null, null);
        updatedCursor.moveToFirst();
        String col_reviews = updatedCursor.getString(COL_REVIEWS);
        String col_videos = updatedCursor.getString(COL_VIDEOS);
        if (!reviews.equals(col_reviews) || !videos.equals(col_videos))
            Log.e(LOG_TAG, "Ouch...");
        Log.d(LOG_TAG, updatedCursor.getString(COL_REVIEWS) + updatedCursor.getString(COL_VIDEOS));
        updatedCursor.close();
    }


}
