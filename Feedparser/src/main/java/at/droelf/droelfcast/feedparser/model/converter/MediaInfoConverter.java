package at.droelf.droelfcast.feedparser.model.converter;

import at.droelf.droelfcast.common.O;
import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.model.parsed.item.MediaInfo;
import at.droelf.droelfcast.feedparser.model.raw.item.Enclosure;
import at.droelf.droelfcast.feedparser.model.raw.item.RawItem;

public class MediaInfoConverter {

    public O<MediaInfo> getMediaInfo(RawItem rawItem){
        final Enclosure enclosure = rawItem.getEnclosure();

        if(enclosure == null){
            return O.n();
        }

        if(!StringUtils.hasLength(enclosure.getLength(), enclosure.getUrl(), enclosure.getType())){
            return O.n();
        }

        return O.c(new MediaInfo(
                enclosure.getUrl(),
                enclosure.getLength(),
                enclosure.getType(),
                StringUtils.ensureEmpty(rawItem.getDuration())
        ));
    }
}
