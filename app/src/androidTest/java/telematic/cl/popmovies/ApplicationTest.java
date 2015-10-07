package telematic.cl.popmovies;

import android.app.Application;
import android.net.Uri;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public void uriTest() {

        final String BASE_MOVIES_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String MOVIES_SORT_PARAM = "sort_by";

        final String API_KEY_PARAM = "api_key";
        final String API_KEY = "facc96b28116b39d12dbe926d2127e7f";

        final String EXPECTED_MOVIE = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=facc96b28116b39d12dbe926d2127e7f";
        final String EXPECTED_POSTER = "http://image.tmdb.org/t/p/w185/fWOPN0XBvHXFYr3RsPr74qBge2I.jpg";

        final String POSTER_PATH_PARAM = "/fWOPN0XBvHXFYr3RsPr74qBge2I.jpg";

        Uri movieUri = Uri.parse(BASE_MOVIES_URL).buildUpon()
                .appendQueryParameter(MOVIES_SORT_PARAM, "popularity.desc")
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        Log.e("Expected", EXPECTED_MOVIE);
        Log.e("Uri!", movieUri.toString());

        final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_WIDTH = "w185";
        ///fWOPN0XBvHXFYr3RsPr74qBge2I.jpg

        Uri posterUri = Uri.parse(BASE_POSTER_URL).buildUpon()
                .appendPath(IMAGE_WIDTH)
                .appendPath(POSTER_PATH_PARAM).build();

        Log.e("Expected", EXPECTED_MOVIE);
        Log.e("Uri!", posterUri.toString());


        assertEquals(EXPECTED_MOVIE, movieUri.toString());
        assertEquals(EXPECTED_POSTER, posterUri.toString());

    }
}