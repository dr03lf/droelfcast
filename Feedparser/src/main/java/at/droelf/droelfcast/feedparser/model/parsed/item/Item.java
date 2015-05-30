package at.droelf.droelfcast.feedparser.model.parsed.item;

import at.droelf.droelfcast.common.O;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageBundle;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkBundle;

public class Item {

    private LinkBundle linkBundle;
    private ItemInfo itemInfo;
    private ImageBundle imageBundle;

    private O<MediaInfo> mediaInfo;

    public Item(LinkBundle linkBundle, ItemInfo itemInfo, ImageBundle imageBundle, O<MediaInfo> mediaInfo) {
        this.linkBundle = linkBundle;
        this.itemInfo = itemInfo;
        this.imageBundle = imageBundle;
        this.mediaInfo = mediaInfo;
    }

    public LinkBundle getLinkBundle() {
        return linkBundle;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public ImageBundle getImageBundle() {
        return imageBundle;
    }

    public O<MediaInfo> getMediaInfo() {
        return mediaInfo;
    }
}
