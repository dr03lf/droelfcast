package at.droelf.droelfcast.feedparser.model.parsed.item;

public class ItemInfo {

    private String title;
    private String pubDate;

    private String shortDescription;
    private String description;
    private String content;

    public ItemInfo(String title, String pubDate, String shortDescription, String description, String content) {
        this.title = title;
        this.pubDate = pubDate;
        this.shortDescription = shortDescription;
        this.description = description;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }
}
