package at.droelf.droelfcast.feedparser.model.item;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Chapter {

    @Attribute(required = true)
    private String start;

    @Attribute(required = true)
    private String title;

    @Attribute(required = false)
    private String href;

    @Attribute(required = false)
    private String image;
}
