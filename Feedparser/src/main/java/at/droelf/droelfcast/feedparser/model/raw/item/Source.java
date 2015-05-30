package at.droelf.droelfcast.feedparser.model.raw.item;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import at.droelf.droelfcast.feedparser.model.raw.annotation.Rss;

@Root(strict = false)
public class Source {

    @Rss
    @Attribute(required = true)
    private String url;

    @Rss
    @Text
    private String value;

}
