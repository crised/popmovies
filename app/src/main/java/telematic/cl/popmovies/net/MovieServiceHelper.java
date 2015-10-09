package telematic.cl.popmovies.net;

import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpUrl;


import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import telematic.cl.popmovies.R;
import telematic.cl.popmovies.util.Movies;
import telematic.cl.popmovies.util.Reviews;
import telematic.cl.popmovies.util.Videos;

import static telematic.cl.popmovies.util.Consts.BASE_MOVIES_URL;

/**
 * Created by crised on 07-10-15.
 */
public class MovieServiceHelper {


    public final String LOG_TAG = MovieServiceHelper.class.getSimpleName();

    private String mAPI_KEY;

    private MovieService mMovieService;

    public MovieServiceHelper(Context context) {
        mAPI_KEY = context.getString(R.string.api_key);
        HttpUrl retrofitUrl = HttpUrl.parse(BASE_MOVIES_URL);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(retrofitUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mMovieService = retrofit.create(MovieService.class);
    }

    // All of the below should be persisted(inserted/updated), into db.
    public List<Movies.Result> fetchMovies() {
        try {
            //TODO put parameter vote_average.desc
            Call<Movies> moviesCall = mMovieService.listMovies("popularity.desc", mAPI_KEY);
            Response<Movies> moviesResponse = moviesCall.execute();
            Log.d(LOG_TAG, String.valueOf(moviesResponse.isSuccess()));
            return moviesResponse.body().getResults();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can't fetch movies, IO exception");
            return null;
        }
    }


    public String fetchReviews(long movieKey) {
        try {
            Call<Reviews> reviewsCall = mMovieService.listReviews(movieKey, mAPI_KEY);
            Response<Reviews> reviewsResponse = reviewsCall.execute();
            if (!reviewsResponse.isSuccess()) return null;
            List<Reviews.Result> reviews = reviewsResponse.body().getResults();
            String reviewsJson = new Gson().toJson(reviews);
            return reviewsJson;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can't fetch movies, IO exception");
            return null;
        }
    }

    public String fetchVideos(long movieKey) {
        try {
            Call<Videos> videosCall = mMovieService.listVideos(movieKey, mAPI_KEY);
            Response<Videos> videosResponse = videosCall.execute();
            if (!videosResponse.isSuccess()) return null;
            List<Videos.Result> videos = videosResponse.body().getResults();
            String videosJson = new Gson().toJson(videos);
            return videosJson;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can't fetch movies, IO exception");
            return null;
        }
    }


}
