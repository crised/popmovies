package telematic.cl.popmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import telematic.cl.popmovies.util.Movies;

/**
 * Created by crised on 22-09-15.
 */
public class ImageAdapter extends BaseAdapter {

    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private List<Movies.Result> movies;


    public ImageAdapter(Context c) {
        mContext = c;
        movies = new ArrayList<>();
    }

    public int getCount() {
        //Log.d(LOG_TAG, "getCount: " + movies.size());
        return movies.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        //   Log.d(LOG_TAG, "getView");
        //Could have Cast (GridView) in ViewGroup parent

        ImageView imageView;
        if (convertView == null) {
//            Log.d(LOG_TAG, "convertView  null");

            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(185, 262)); //info on each of children
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);

        } else {
            //         Log.d(LOG_TAG, "convertView not  null");

            imageView = (ImageView) convertView;
        }

        Movies.Result movie = movies.get(position);
        if (movie != null)
            Picasso.with(mContext).load(movie.getPosterUri()).into(imageView);


        //if (movies.size() == 20) Log.d(LOG_TAG, "Adapter is filled with movies");

        return imageView;
    }


    public List<Movies.Result> getMovies() {
        return movies;
    }
}
