package telematic.cl.popmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import telematic.cl.popmovies.data.MovieContract;
import telematic.cl.popmovies.data.MovieDbHelper;
import telematic.cl.popmovies.utils.PollingCheck;

import static telematic.cl.popmovies.data.MovieContract.MovieEntry.*;

/**
 * Created by crised on 06-10-15.
 */
public class TestUtilities extends AndroidTestCase {

    static ContentValues createMovieValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(COLUMN_MOVIE_KEY, 12);
        testValues.put(COLUMN_LANGUAGE, "en");
        testValues.put(COLUMN_OVERVIEW, "this is a movie about ...");
        testValues.put(COLUMN_DATE, "Jan 3");
        testValues.put(COLUMN_POSTER_PATH, "/Uxji.jpg");
        testValues.put(COLUMN_POPULARITY, 21);
        testValues.put(COLUMN_TITLE, "The Dabbler");
        testValues.put(COLUMN_VOTE_AVG, 8);
        testValues.put(COLUMN_VOTE_COUNT, 123);
        testValues.put(COLUMN_FAVORITE, 0);
        testValues.put(COLUMN_REVIEWS, "");
        testValues.put(COLUMN_VIDEOS, "");
        return testValues;
    }

    static ContentValues createMovieValues(int id) {
        ContentValues testValues = new ContentValues();
        testValues.put(COLUMN_MOVIE_KEY, id);
        testValues.put(COLUMN_LANGUAGE, "en");
        testValues.put(COLUMN_OVERVIEW, "this is a movie about ...");
        testValues.put(COLUMN_DATE, "Jan 3");
        testValues.put(COLUMN_POSTER_PATH, "/Uxji.jpg");
        testValues.put(COLUMN_POPULARITY, 21);
        testValues.put(COLUMN_TITLE, "The Dabbler");
        testValues.put(COLUMN_VOTE_AVG, 8);
        testValues.put(COLUMN_VOTE_COUNT, 123);
        testValues.put(COLUMN_FAVORITE, 0);
        testValues.put(COLUMN_REVIEWS, "");
        testValues.put(COLUMN_VIDEOS, "");
        return testValues;
    }

    static long insertMovieValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = createMovieValues();
        long locationRowId;
        //directly into the db, not through content provider
        locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);
        return locationRowId;
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

}
