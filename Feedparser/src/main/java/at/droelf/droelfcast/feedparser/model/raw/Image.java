package at.droelf.droelfcast.feedparser.model.raw;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import at.droelf.droelfcast.feedparser.model.raw.annotation.Itunes;
import at.droelf.droelfcast.feedparser.model.raw.annotation.Rss;

@Root(strict = true)
public class Image {

    @Itunes
    @Attribute(required = false)
    private String href;

    @Rss
    @Element(required = false)
    private String url;

    @Rss
    @Element(required = false)
    private String title;

    @Rss
    @Element(required = false)
    private String link;

    @Rss
    @Element(required = false)
    private String description;

    @Rss
    @Element(required = false)
    private Integer width;

    @Rss
    @Element(required = false)
    private Integer height;

}
