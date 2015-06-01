package at.droelf.droelfcast.backend.model;

public class FeedResponse{
    private String cacheKey;
    private String url;

    public FeedResponse(String cacheKey, String url) {
        this.cacheKey = cacheKey;
        this.url = url;
    }

    public String getUrl() {
        return url;
        }

    public String getCacheKey() {
        return cacheKey;
    }
}