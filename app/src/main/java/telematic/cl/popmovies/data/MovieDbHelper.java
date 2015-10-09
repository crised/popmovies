package telematic.cl.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static telematic.cl.popmovies.data.MovieContract.MovieEntry.*;

/**
 * Created by crised on 06-10-15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_MOVIE_KEY + " INTEGER NOT NULL UNIQUE, " +
                COLUMN_LANGUAGE + " TEXT, " +
                COLUMN_OVERVIEW + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_POSTER_PATH + " TEXT, " +
                COLUMN_POPULARITY + " REAL, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_VOTE_AVG + " REAL, " +
                COLUMN_VOTE_COUNT + " INTEGER, " +
                COLUMN_FAVORITE + " INTEGER NOT NULL, " +
                COLUMN_REVIEWS + " TEXT, " +
                COLUMN_VIDEOS + " Text"


                + ")";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
