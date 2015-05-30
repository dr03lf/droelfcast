package at.droelf.droelfcast.feedparser.model.parsed.item;

public class MediaInfo {

    private String url;
    private String length;
    private String type;
    private String duration;

    public MediaInfo(String url, String length, String type, String duration) {
        this.url = url;
        this.length = length;
        this.type = type;
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public String getLength() {
        return length;
    }

    public String getType() {
        return type;
    }

    public String getDuration() {
        return duration;
    }
}
