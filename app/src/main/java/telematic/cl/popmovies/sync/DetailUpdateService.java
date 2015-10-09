package telematic.cl.popmovies.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import telematic.cl.popmovies.net.MovieServiceHelper;

import static telematic.cl.popmovies.util.Consts.*;
import static telematic.cl.popmovies.data.MovieContract.MovieEntry.*;

/**
 * Created by crised on 08-10-15.
 */
public class DetailUpdateService extends IntentService {

    public DetailUpdateService() {
        super("DetailUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Uri movieUri = intent.getParcelableExtra(MOVIE_ID);
        if (movieUri == null) return;
        Cursor cursor = getContentResolver().query(movieUri, null, null, null, null);
        cursor.moveToFirst();
        long movieKey = cursor.getLong(COL_MOVIE_KEY);
        MovieServiceHelper serviceHelper = new MovieServiceHelper(this);
        String reviews = serviceHelper.fetchReviews(movieKey);
        String videos = serviceHelper.fetchVideos(movieKey);
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(COLUMN_REVIEWS, reviews);
        updatedValues.put(COLUMN_VIDEOS, videos);
        getContentResolver().update(movieUri, updatedValues, null, new String[]{String.valueOf(movieKey)});

        //delete all details from non favorites, as mainteinance.
    }

// Add a Uri instance to an Intent
    // intent.putExtra("imageUri", uri);

    // Get a Uri from an Intent
//    Uri uri = intent.getParcelableExtra("imageUri");


}
