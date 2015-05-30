package at.droelf.droelfcast.feedparser.model.converter;

import java.util.ArrayList;
import java.util.List;

import at.droelf.droelfcast.common.CollectionUtils;
import at.droelf.droelfcast.common.O;
import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageBundle;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageItunes;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageRss;
import at.droelf.droelfcast.feedparser.model.raw.RawImage;

public class ImageConverter {

    public ImageBundle getImageBundle(List<RawImage> rawImages){
        final List<RawImage> rawRssImages = new ArrayList<>();
        final List<RawImage> rawItunesImage = new ArrayList<>();

        for(RawImage rawImage : rawImages){
            if(StringUtils.hasLength(rawImage.getUrl(), rawImage.getTitle(), rawImage.getLink())){
                rawRssImages.add(rawImage);

            } else if(StringUtils.hasLength(rawImage.getHref())){
                rawItunesImage.add(rawImage);
            }
        }

        return new ImageBundle(
                parseItunesImage(rawItunesImage),
                parseRssImage(rawRssImages)
        );
    }


    private O<ImageRss> parseRssImage(List<RawImage> rawImages){
        final O<RawImage> firstElement = CollectionUtils.getFirstElement(rawImages);

        if(!firstElement.e()){
            return O.c(
                    new ImageRss(
                            firstElement.d().getUrl(),
                            firstElement.d().getTitle(),
                            firstElement.d().getLink()
                    )
            );
        }

        return O.n();
    }

    private O<ImageItunes> parseItunesImage(List<RawImage> rawImages){
        final O<RawImage> firstElement = CollectionUtils.getFirstElement(rawImages);

        if(!firstElement.e()){
            return O.c(
                new ImageItunes(firstElement.d().getHref())
            );
        }

        return O.n();
    }

}
