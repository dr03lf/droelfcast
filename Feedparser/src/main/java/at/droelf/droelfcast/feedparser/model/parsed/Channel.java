package at.droelf.droelfcast.feedparser.model.parsed;

import java.util.List;

import at.droelf.droelfcast.feedparser.model.parsed.image.ImageBundle;
import at.droelf.droelfcast.feedparser.model.parsed.item.Item;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkBundle;

public class Channel {

    private ChannelInfo channelInfo;
    private LinkBundle linkBundle;
    private ImageBundle imageBundle;

    private List<Item> items;


    public Channel(ChannelInfo channelInfo, LinkBundle linkBundle, ImageBundle imageBundle, List<Item> items) {
        this.channelInfo = channelInfo;
        this.linkBundle = linkBundle;
        this.imageBundle = imageBundle;
        this.items = items;
    }

    public ChannelInfo getChannelInfo() {
        return channelInfo;
    }

    public LinkBundle getLinkBundle() {
        return linkBundle;
    }

    public ImageBundle getImageBundle() {
        return imageBundle;
    }

    public List<Item> getItems() {
        return items;
    }
}
