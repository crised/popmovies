package telematic.cl.popmovies.util;

/**
 * Created by crised on 05-10-15.
 */

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static telematic.cl.popmovies.util.Consts.BASE_POSTER_URL;
import static telematic.cl.popmovies.util.Consts.IMAGE_WIDTH;

public class Movies {

    private Integer page;
    private List<Result> results = new ArrayList<Result>();
    private Integer totalPages;
    private Integer totalResults;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();



    public class Result {

        private Boolean adult;
        private String backdropPath;
        private List<String> genreIds = new ArrayList<String>();
        private Integer id;
        private String originalLanguage;
        private String originalTitle;
        private String overview;
        private String releaseDate;
        private String posterPath;
        private Double popularity;
        private String title;
        private Boolean video;
        private Double voteAverage;
        private Integer voteCount;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Uri getPosterUri(){

            return Uri.parse(BASE_POSTER_URL).buildUpon()
                    .appendPath(IMAGE_WIDTH)
                    .appendEncodedPath(posterPath)
                    .build();


        }

        public Boolean getAdult() {
            return adult;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public List<String> getGenreIds() {
            return genreIds;
        }

        public Integer getId() {
            return id;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public String getOverview() {
            return overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public Double getPopularity() {
            return popularity;
        }

        public String getTitle() {
            return title;
        }

        public Boolean getVideo() {
            return video;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }
    }

    public Integer getPage() {
        return page;
    }

    public List<Result> getResults() {
        return results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

}