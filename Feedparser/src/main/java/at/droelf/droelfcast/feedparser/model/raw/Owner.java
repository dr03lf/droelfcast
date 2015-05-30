package at.droelf.droelfcast.feedparser.model.raw;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import at.droelf.droelfcast.feedparser.model.raw.annotation.Itunes;

@Root(strict = false)
public class Owner {

    @Itunes
    @Element(required = false)
    private String name;

    @Itunes
    @Element(required = false)
    private String email;

}
