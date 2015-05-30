package at.droelf.droelfcast.feedparser.model.parsed.image;

public class ImageRss implements Image{

    private String url;
    private String title;
    private String link;

    public ImageRss(String url, String title, String link) {
        this.url = url;
        this.title = title;
        this.link = link;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
