package at.droelf.droelfcast.feedparser.model.item;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import at.droelf.droelfcast.feedparser.model.annotation.Rss;

@Root(strict = true)
public class Guid {

    @Rss
    @Attribute(required = false)
    private String isPermaLink;

    @Rss
    @Text
    private String guid;

}
