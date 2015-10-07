package telematic.cl.popmovies.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;

import telematic.cl.popmovies.data.MovieDbHelper;

import static telematic.cl.popmovies.data.MovieContract.MovieEntry.*;
import static telematic.cl.popmovies.data.MovieDbHelper.*;
import static telematic.cl.popmovies.db.TestUtilities.*;

/**
 * Created by crised on 06-10-15.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {

        deleteTheDatabase();

        SQLiteDatabase db = new MovieDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        Log.d(LOG_TAG, c.getString(0));

        //assertEquals();

        c = db.rawQuery("PRAGMA table_info(" + TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> movieColumns = new HashSet<>();

        movieColumns.add(_ID);
        movieColumns.add(COLUMN_MOVIE_KEY);
        movieColumns.add(COLUMN_LANGUAGE);
        movieColumns.add(COLUMN_OVERVIEW);
        movieColumns.add(COLUMN_DATE);
        movieColumns.add(COLUMN_POSTER_PATH);
        movieColumns.add(COLUMN_POPULARITY);
        movieColumns.add(COLUMN_TITLE);
        movieColumns.add(COLUMN_VOTE_AVG);
        movieColumns.add(COLUMN_VOTE_COUNT);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumns.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movieColumns.isEmpty());
        db.close();

    }


    public void testinsertMovie() {

        SQLiteDatabase db = new MovieDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        long movieRowId;
        ContentValues testValues = createMovieValues();

        movieRowId = db.insert(TABLE_NAME, null, testValues);
        assertTrue(movieRowId != -1); //WE got a row back

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from location query",
                cursor.moveToNext());


        cursor.close();
        db.close();


    }

}
