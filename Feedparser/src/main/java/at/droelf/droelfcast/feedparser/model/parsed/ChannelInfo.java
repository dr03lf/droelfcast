package at.droelf.droelfcast.feedparser.model.parsed;


public class ChannelInfo {

    private final String description;
    private final String shortDescription;

    private final String title;
    private final String author;
    private final String language;


    public ChannelInfo(String description, String shortDescription, String title, String author, String language) {
        this.description = description;
        this.shortDescription = shortDescription;
        this.title = title;
        this.author = author;
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLanguage() {
        return language;
    }
}
