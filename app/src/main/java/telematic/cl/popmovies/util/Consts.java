package telematic.cl.popmovies.util;

/**
 * Created by crised on 22-09-15.
 */
public final class Consts {

    private Consts() {
    }

    public static final String API_KEY_PARAM = "api_key";
    public static final String BASE_MOVIES_URL = "http://api.themoviedb.org";
    public static final String MOVIES_SORT_PARAM = "sort_by";
    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p";
    public static final String IMAGE_WIDTH = "w185";
    public static final String MOVIE_ID = "movie_id";

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_KEY = 1;
    public static final int COL_LANGUAGE = 2;
    public static final int COL_OVERVIEW = 3;
    public static final int COL_DATE = 4;
    public static final int COL_POSTER_PATH = 5;
    public static final int COL_POPULARITY = 6;
    public static final int COL_TITLE = 7;
    public static final int COL_VOTE_AVG = 8;
    public static final int COL_VOTE_COUNT = 9;
    public static final int COL_FAVORITE = 10;
    public static final int COL_REVIEWS = 11;
    public static final int COL_VIDEOS = 12;
}