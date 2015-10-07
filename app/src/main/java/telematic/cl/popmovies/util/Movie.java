package telematic.cl.popmovies.util;

import android.net.Uri;

/**
 * Created by crised on 22-09-15.
 */
public class Movie {

    private String title;
    private Uri uri;
    private String plot; //overview
    private String rating; //vote_average
    private String date;


    public Movie(String title, Uri uri, String plot, String rating, String date) {
        this.title = title;
        this.uri = uri;
        this.plot = plot;
        this.rating = rating;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public Uri getUri() {
        return uri;
    }

    public String getPlot() {
        return plot;
    }

    public String getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }
}
