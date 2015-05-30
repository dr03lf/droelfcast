package at.droelf.droelfcast.feedparser.model.raw;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Feed {

    @Element(required = true)
    private RawChannel channel;

    @Attribute(required = true)
    private String version;


    public RawChannel getChannel() {
        return channel;
    }

    public String getVersion() {
        return version;
    }
}
