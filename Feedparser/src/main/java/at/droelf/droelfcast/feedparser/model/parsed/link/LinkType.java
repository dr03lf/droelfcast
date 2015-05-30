package at.droelf.droelfcast.feedparser.model.parsed.link;

public enum LinkType {

    ALTERNATE("alternate"),
    PAGED_NEXT("next"),
    PAGED_FIRST("first"),
    PAGED_LAST("last"),
    DISTRI_HUB("hub"),
    DISTRI_SELF("self"),
    RSS_LINK(null),
    PAYMENT("payment")
    ;


    private String rel;

    LinkType(String rel){
        this.rel = rel;
    }


    public String getRel() {
        return rel;
    }
}
