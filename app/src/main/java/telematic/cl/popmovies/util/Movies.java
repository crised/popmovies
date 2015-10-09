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

        private transient Integer _id;
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
        private transient Integer favorite;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Uri getPosterUri() {
            return Uri.parse(BASE_POSTER_URL).buildUpon()
                    .appendPath(IMAGE_WIDTH)
                    .appendEncodedPath(posterPath)
                    .build();
        }

        public Integer get_id() {
            return _id;
        }

        public void set_id(Integer _id) {
            this._id = _id;
        }

        public Boolean getAdult() {
            return adult;
        }

        public void setAdult(Boolean adult) {
            this.adult = adult;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public List<String> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<String> genreIds) {
            this.genreIds = genreIds;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public Double getPopularity() {
            return popularity;
        }

        public void setPopularity(Double popularity) {
            this.popularity = popularity;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Boolean getVideo() {
            return video;
        }

        public void setVideo(Boolean video) {
            this.video = video;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getFavorite() {
            return favorite;
        }

        public void setFavorite(Integer favorite) {
            this.favorite = favorite;
        }

        public Map<String, Object> getAdditionalProperties() {
            return additionalProperties;
        }

        public void setAdditionalProperties(Map<String, Object> additionalProperties) {
            this.additionalProperties = additionalProperties;
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