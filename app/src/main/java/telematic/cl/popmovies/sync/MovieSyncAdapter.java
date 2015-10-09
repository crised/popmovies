package telematic.cl.popmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Vector;

import telematic.cl.popmovies.R;
import telematic.cl.popmovies.data.MovieContract;
import telematic.cl.popmovies.net.MovieServiceHelper;
import telematic.cl.popmovies.util.Movies;


/**
 * Created by crised on 07-10-15.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    private Context mContext;

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    private List<Movies.Result> mMovies;
    private String mReviewsJson;
    private String mTrailerJson;

    private Vector<ContentValues> mcVVector;

    private static final String sMoviesFavorites =
            MovieContract.MovieEntry.TABLE_NAME + "." +
                    MovieContract.MovieEntry.COLUMN_FAVORITE
                    + " = ?";


    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        mMovies = new MovieServiceHelper(mContext).fetchMovies();
        if (mMovies == null) return; // no net
        fillCVVector();
        if (mcVVector.size() == 0) return;
        ContentValues[] cvArray = new ContentValues[mcVVector.size()];
        mcVVector.toArray(cvArray);
        Integer deletedRowsNonFavorite;
        deletedRowsNonFavorite = getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, sMoviesFavorites, new String[]{"0"});
        Log.d(LOG_TAG, "Deleted:  " + deletedRowsNonFavorite + " non favorites rows.");
        getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        Log.d(LOG_TAG, "Sync Complete. " + mcVVector.size() + " Inserted");
        // deletedRowsNonFavorite = getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, sMoviesFavorites, new String[]{"0"});
        //Log.d(LOG_TAG, "Deleted:  " + deletedRowsNonFavorite + " non favorites rows.");
        //notifyWeather(); //checking the last update and notify if it' the first of the day
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


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
