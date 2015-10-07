package telematic.cl.popmovies;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import telematic.cl.popmovies.util.Movies;
import telematic.cl.popmovies.util.Reviews;
import telematic.cl.popmovies.util.Videos;

import static telematic.cl.popmovies.util.Consts.*;

/**
 * Created by crised on 05-10-15.
 */
public interface MovieService {

    @GET("/3/discover/movie")
    Call<Movies> listMovies(@Query(MOVIES_SORT_PARAM) String sort,
                            @Query(API_KEY_PARAM) String apikey);

    @GET("3/movie/{id}/reviews")
    Call<Reviews> listReviews(@Path("id") Integer id, @Query(API_KEY_PARAM) String apikey);

    @GET("3/movie/{id}/videos")
    Call<Videos> listVideos(@Path("id") Integer id, @Query(API_KEY_PARAM) String apikey);




}
