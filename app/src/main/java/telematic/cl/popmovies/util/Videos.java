package telematic.cl.popmovies.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by crised on 07-10-15.
 */
public class Videos {

    private Integer page;
    private List<Result> results = new ArrayList<Result>();
    private Integer totalPages;
    private Integer totalResults;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public class Result {

        private String id;
        private String iso6391;
        private String key;
        private String name;
        private String site;
        private Integer size;
        private String type;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


        public String getId() {
            return id;
        }

        public String getIso6391() {
            return iso6391;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public Integer getSize() {
            return size;
        }

        public String getType() {
            return type;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
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
