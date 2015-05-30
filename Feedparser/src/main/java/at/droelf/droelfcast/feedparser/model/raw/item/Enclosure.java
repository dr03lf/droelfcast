package at.droelf.droelfcast.feedparser.model.raw.item;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import at.droelf.droelfcast.feedparser.model.raw.annotation.Rss;

@Root(strict = true)
public class Enclosure {

    @Rss
    @Attribute(required = true)
    private String url;

    @Rss
    @Attribute(required = true)
    private String length;

    @Rss
    @Attribute(required = true)
    private String type;


    public String getUrl() {
        return url;
    }

    public String getLength() {
        return length;
    }

    public String getType() {
        return type;
    }
}
