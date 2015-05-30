package at.droelf.droelfcast.feedparser.model.parsed.link;

public class LinkPayment extends LinkSimple {

    private final String title;
    private final String type;

    public LinkPayment(String link, String title, String type) {
        super(LinkType.PAYMENT, link);
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
