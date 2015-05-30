package at.droelf.droelfcast.feedparser.model.converter;

import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.model.parsed.item.ItemInfo;
import at.droelf.droelfcast.feedparser.model.raw.item.RawItem;

public class ItemInfoConverter {

    public ItemInfo getItemInfo(RawItem item){
        return new ItemInfo(
                StringUtils.ensureEmpty(item.getTitle()),
                StringUtils.ensureEmpty(item.getPubDate()),
                StringUtils.ensureEmpty(item.getSubtitle()),
                getDescription(item),
                StringUtils.ensureEmpty(item.getContent())
        );
    }

    private String getDescription(RawItem rawItem){
        if(StringUtils.hasLength(rawItem.getSummary())){
            return rawItem.getSummary();
        }

        if(StringUtils.hasLength(rawItem.getDescription())){
            return rawItem.getDescription();
        }

        return StringUtils.EMPTY_STRING;
    }
}
