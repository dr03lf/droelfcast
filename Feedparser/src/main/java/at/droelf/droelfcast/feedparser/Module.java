package at.droelf.droelfcast.feedparser;

import org.simpleframework.xml.core.Persister;

import at.droelf.droelfcast.feedparser.model.converter.ChannelConverter;
import at.droelf.droelfcast.feedparser.model.converter.ChannelInfoConverter;
import at.droelf.droelfcast.feedparser.model.converter.ImageConverter;
import at.droelf.droelfcast.feedparser.model.converter.ItemConverter;
import at.droelf.droelfcast.feedparser.model.converter.ItemInfoConverter;
import at.droelf.droelfcast.feedparser.model.converter.LinkConverter;
import at.droelf.droelfcast.feedparser.model.converter.MediaInfoConverter;
import dagger.Provides;

@dagger.Module
public class Module {

    @Provides
    Persister providesPersister(){
        return new Persister();
    }

    @Provides
    LinkConverter providesLinkConverter(){
        return new LinkConverter();
    }

    @Provides
    ItemInfoConverter providesItemInfoConverter(){
        return new ItemInfoConverter();
    }

    @Provides
    ImageConverter providesImageConverter(){
        return new ImageConverter();
    }

    @Provides
    MediaInfoConverter providesMediainfoConverter(){
        return new MediaInfoConverter();
    }

    @Provides
    ItemConverter providesItemConverter(LinkConverter linkConverter, ImageConverter imageConverter, ItemInfoConverter itemInfoConverter, MediaInfoConverter mediainfoConverter){
        return new ItemConverter(linkConverter, itemInfoConverter, imageConverter, mediainfoConverter);
    }

    @Provides
    ChannelInfoConverter providesChannelInfoConverter(){
        return new ChannelInfoConverter();
    }

    @Provides
    ChannelConverter providesChannelConverter(LinkConverter linkConverter, ImageConverter imageConverter, ChannelInfoConverter channelInfoConverter, ItemConverter itemConverter){
        return new ChannelConverter(linkConverter, imageConverter, channelInfoConverter, itemConverter);
    }

}
