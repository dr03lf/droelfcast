package at.droelf.droelfcast.feedparser.model.raw;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import at.droelf.droelfcast.feedparser.model.raw.annotation.Atom;
import at.droelf.droelfcast.feedparser.model.raw.annotation.Rss;

@Root(strict = true)
public class RawLink {

    @Atom
    @Attribute(required = false)
    private String href;

    @Atom
    @Attribute(required = false)
    private String rel;

    @Atom
    @Attribute(required = false)
    private String type;

    @Atom
    @Attribute(required = false)
    private String hreflang;

    @Atom
    @Attribute(required = false)
    private String title;

    @Atom
    @Attribute(required = false)
    private String length;

    @Rss
    @Text(required = false)
    private String link;


    public String getHref() {
        return href;
    }

    public String getRel() {
        return rel;
    }

    public String getType() {
        return type;
    }

    public String getHreflang() {
        return hreflang;
    }

    public String getTitle() {
        return title;
    }

    public String getLength() {
        return length;
    }

    public String getLink() {
        return link;
    }
}
