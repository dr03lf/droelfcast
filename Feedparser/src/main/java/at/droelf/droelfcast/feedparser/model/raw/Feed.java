package at.droelf.droelfcast.feedparser.model.raw;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Feed {

    @Element(required = true)
    private Channel channel;

    @Attribute(required = true)
    private String version;


    public Channel getChannel() {
        return channel;
    }

    public String getVersion() {
        return version;
    }
}
