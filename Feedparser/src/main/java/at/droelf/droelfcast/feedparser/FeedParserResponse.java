package at.droelf.droelfcast.feedparser;

import at.droelf.droelfcast.feedparser.model.parsed.Channel;

public class FeedParserResponse {

    private String url;
    private Channel channel;

    public FeedParserResponse(String url, Channel channel) {
        this.url = url;
        this.channel = channel;
    }

    public String getUrl() {
        return url;
    }

    public Channel getChannel() {
        return channel;
    }
}
