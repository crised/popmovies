package telematic.cl.popmovies.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by crised on 07-10-15.
 */
public class Reviews {

    private Integer page;
    private List<Result> results = new ArrayList<Result>();
    private Integer totalPages;
    private Integer totalResults;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public class Result {

        private String id;
        private String author;
        private String content;
        private String url;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
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
