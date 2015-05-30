package at.droelf.droelfcast.feedparser.model.parsed;

import java.util.List;

import at.droelf.droelfcast.common.O;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkAlternateFeed;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkPagedFeed;

public class Channel {

    private final List<LinkAlternateFeed> linkAlternateFeeds;
    private final O<LinkPagedFeed> linkPagedFeeds;

    public Channel(List<LinkAlternateFeed> linkAlternateFeeds, O<LinkPagedFeed> linkPagedFeeds) {
        this.linkAlternateFeeds = linkAlternateFeeds;
        this.linkPagedFeeds = linkPagedFeeds;
    }
}
