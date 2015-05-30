package at.droelf.droelfcast.feedparser.model.parsed.link;
import java.util.List;

import at.droelf.droelfcast.common.O;

public class LinkBundle {

    private final List<LinkAlternateFeed> linkAlternateFeeds;
    private final O<LinkPagedFeed> linkPagedFeeds;
    private final O<LinkRss> linkRss;
    private final O<LinkPayment> linkPayment;
    //TODO hub, self


    public LinkBundle(List<LinkAlternateFeed> linkAlternateFeeds, O<LinkPagedFeed> linkPagedFeeds, O<LinkRss> linkRss, O<LinkPayment> linkPayment) {
        this.linkAlternateFeeds = linkAlternateFeeds;
        this.linkPagedFeeds = linkPagedFeeds;
        this.linkRss = linkRss;
        this.linkPayment = linkPayment;
    }

    public List<LinkAlternateFeed> getLinkAlternateFeeds() {
        return linkAlternateFeeds;
    }

    public O<LinkPagedFeed> getLinkPagedFeeds() {
        return linkPagedFeeds;
    }

    public O<LinkRss> getLinkRss() {
        return linkRss;
    }

    public O<LinkPayment> getLinkPayment() {
        return linkPayment;
    }
}
