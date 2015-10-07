package telematic.cl.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import telematic.cl.popmovies.util.Movie;
import telematic.cl.popmovies.util.Movies;
import telematic.cl.popmovies.util.Reviews;
import telematic.cl.popmovies.util.Videos;

import static telematic.cl.popmovies.util.Consts.API_KEY_PARAM;
import static telematic.cl.popmovies.util.Consts.BASE_MOVIES_URL;
import static telematic.cl.popmovies.util.Consts.BASE_POSTER_URL;
import static telematic.cl.popmovies.util.Consts.IMAGE_WIDTH;
import static telematic.cl.popmovies.util.Consts.MOVIES_SORT_PARAM;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {


    private static final String LOG_TAG = MainFragment.class.getSimpleName();


    private ImageAdapter mAdapter;

    public MainFragment() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        syncNet();
        super.onStart();
    }

    private void syncNet() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String preference = prefs.getString(getString(R.string.settings_key),
                getString(R.string.settings_default));
        new NetTask().execute(preference);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAdapter = new ImageAdapter(getContext());


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movie = mAdapter.getMovies().get(position);
                Intent intent = new Intent(getActivity(), detail.class)
                        .putExtra("title", movie.getTitle())
                        .putExtra("uri", movie.getUri().toString())
                        .putExtra("plot", movie.getPlot())
                        .putExtra("rating", movie.getRating())
                        .putExtra("date", movie.getDate());
                startActivity(intent);
            }
        });


        return gridview;
    }


    public class NetTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mAdapter.getMovies().clear();
            mAdapter.getMovies().addAll(movies);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            String API_KEY = getString(R.string.api_key);

            Uri movieUri = Uri.parse("http://api.themoviedb.org/3/discover/movie?").buildUpon()
                    .appendQueryParameter(MOVIES_SORT_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build(); // n results

                /* Call<Reviews> reviewsCall = service.listReviews(135397, API_KEY);
                Response<Reviews> reviewsResponse = reviewsCall.execute();
                Log.d(LOG_TAG, String.valueOf(reviewsResponse.isSuccess()));
                List<Reviews.Result> reviewses = reviewsResponse.body().getResults();
                Log.d(LOG_TAG, reviewses.get(0).getContent());
                Log.d(LOG_TAG, reviewses.get(0).getUrl());
                Log.d(LOG_TAG, reviewses.get(1).getUrl());

                Call<Videos> videosCall = service.listVideos(135397, API_KEY);
                Response<Videos> videosResponse = videosCall.execute();
                Log.d(LOG_TAG, String.valueOf(videosResponse.isSuccess()));
                List<Videos.Result> videoses = videosResponse.body().getResults();
                Log.d(LOG_TAG, videoses.get(0).getId());
                Log.d(LOG_TAG, videoses.get(0).getKey());
                Log.d(LOG_TAG, videoses.get(1).getKey());*/


            String jsonString = httpGET(movieUri.toString());
            if (jsonString == null) return null;

            List<Movie> movies = jsonParser(jsonString);
            return movies;
        }


        private List<Movie> jsonParser(String jsonString) {

            try {
                List<Movie> movies = new ArrayList<>();
                JSONObject parentJson = new JSONObject(jsonString);
                JSONArray array = parentJson.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    Uri uriPoster = Uri.parse(BASE_POSTER_URL).buildUpon()
                            .appendPath(IMAGE_WIDTH)
                            .appendEncodedPath(jsonObject.getString("poster_path"))
                            .build(); //trim slash at poster_path

                    Movie movie = new Movie(
                            jsonObject.getString("original_title"),
                            uriPoster,
                            jsonObject.getString("overview"),
                            jsonObject.getString("vote_average"),
                            jsonObject.getString("release_date")

                    );

                    movies.add(movie);
                }
                return movies;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Parsing JSON Error", e);
                return null;
            }
        }

        private String httpGET(String urlString) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) buffer.append(line);

                if (buffer.length() == 0) return null;

                return buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

            }
        }
    }
}