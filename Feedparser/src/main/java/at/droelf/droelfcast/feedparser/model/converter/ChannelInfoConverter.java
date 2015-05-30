package at.droelf.droelfcast.feedparser.model.converter;


import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.model.parsed.ChannelInfo;
import at.droelf.droelfcast.feedparser.model.raw.RawChannel;

public class ChannelInfoConverter {

    public ChannelInfo getChannelInfo(RawChannel channel){
        return new ChannelInfo(
                getDescription(channel),
                StringUtils.ensureEmpty(channel.getSubtitle()),
                StringUtils.ensureEmpty(channel.getTitle()),
                StringUtils.ensureEmpty(channel.getAuthor()),
                StringUtils.ensureEmpty(channel.getLanguage())
        );
    }

    private String getDescription(RawChannel channel){
        if(StringUtils.hasLength(channel.getSummary())){
            return channel.getSummary();
        }

        if(StringUtils.hasLength(channel.getDescription())){
            return channel.getDescription();
        }

        return StringUtils.EMPTY_STRING;
    }

}
