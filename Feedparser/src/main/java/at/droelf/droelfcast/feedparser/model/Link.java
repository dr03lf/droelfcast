package at.droelf.droelfcast.feedparser.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import at.droelf.droelfcast.feedparser.model.annotation.Atom;
import at.droelf.droelfcast.feedparser.model.annotation.Rss;

@Root(strict = true)
public class Link {

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
}
