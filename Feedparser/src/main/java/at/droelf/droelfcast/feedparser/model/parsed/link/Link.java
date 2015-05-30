package at.droelf.droelfcast.feedparser.model.parsed.link;

import at.droelf.droelfcast.common.StringUtils;

public class Link {

    private LinkType linkType;
    private String link;
    private String rel;

    public Link(LinkType linkType, String link) {
        this.linkType = linkType;
        this.link = link;
        this.rel = StringUtils.ensureEmpty(linkType.getRel());
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public String getLink() {
        return link;
    }

    public String getRel() {
        return rel;
    }
}
