package at.droelf.droelfcast.feedparser.model.converter;

import javax.inject.Inject;

import at.droelf.droelfcast.common.O;
import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageBundle;
import at.droelf.droelfcast.feedparser.model.parsed.item.Item;
import at.droelf.droelfcast.feedparser.model.parsed.item.ItemInfo;
import at.droelf.droelfcast.feedparser.model.parsed.item.MediaInfo;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkBundle;
import at.droelf.droelfcast.feedparser.model.raw.item.RawItem;
import dagger.Component;

public class ItemConverter {

    private LinkConverter linkConverter;
    private ItemInfoConverter itemInfoConverter;
    private ImageConverter imageConverter;
    private MediaInfoConverter mediainfoConverter;


    public ItemConverter(LinkConverter linkConverter, ItemInfoConverter itemInfoConverter, ImageConverter imageConverter, MediaInfoConverter mediainfoConverter) {
        this.linkConverter = linkConverter;
        this.itemInfoConverter = itemInfoConverter;
        this.imageConverter = imageConverter;
        this.mediainfoConverter = mediainfoConverter;
    }

    public Item getItem(RawItem rawItem){

        final LinkBundle linkBundle = linkConverter.getLinkBundle(rawItem.getLinks());
        final ItemInfo itemInfo = itemInfoConverter.getItemInfo(rawItem);
        final ImageBundle imageBundle = imageConverter.getImageBundle(rawItem.getImages());
        final O<MediaInfo> mediaInfo = mediainfoConverter.getMediaInfo(rawItem);

        return new Item(
            linkBundle,
            itemInfo,
            imageBundle,
            mediaInfo
        );
    }

}
