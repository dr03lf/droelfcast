package at.droelf.droelfcast.feedparser.model.parsed.link;

public class LinkAlternateFeed extends Link{

    private final String title;
    private final String type;

    public LinkAlternateFeed(String link, String title, String type) {
        super(LinkType.ALTERNATE, link);
        this.title = title;
        this.type = type;
    }
}
