package at.droelf.droelfcast.feedparser.model.converter;

import java.util.ArrayList;
import java.util.List;

import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.feedparser.model.parsed.ChannelInfo;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageBundle;
import at.droelf.droelfcast.feedparser.model.parsed.item.Item;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkBundle;
import at.droelf.droelfcast.feedparser.model.raw.RawChannel;
import at.droelf.droelfcast.feedparser.model.raw.item.RawItem;


public class ChannelConverter {

    private final LinkConverter linkConverter;
    private final ImageConverter imageConverter;
    private final ChannelInfoConverter channelInfoConverter;
    private final ItemConverter itemConverter;

    public ChannelConverter(LinkConverter linkConverter, ImageConverter imageConverter, ChannelInfoConverter channelInfoConverter, ItemConverter itemConverter) {
        this.linkConverter = linkConverter;
        this.imageConverter = imageConverter;
        this.channelInfoConverter = channelInfoConverter;
        this.itemConverter = itemConverter;
    }

    public Channel getChannel(RawChannel rawChannel){

        final ChannelInfo channelInfo = channelInfoConverter.getChannelInfo(rawChannel);
        final LinkBundle linkBundle = linkConverter.getLinkBundle(rawChannel.getLinks());
        final ImageBundle imageBundle = imageConverter.getImageBundle(rawChannel.getImages());

        final List<Item> itemList = new ArrayList<>();
        for(RawItem rawItem : rawChannel.getItems()){
            itemList.add(itemConverter.getItem(rawItem));
        }

        return new Channel(
            channelInfo,
                linkBundle,
                imageBundle,
                itemList
        );
    }


}
