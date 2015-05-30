package at.droelf.droelfcast.feedparser.model.parsed.link;

import at.droelf.droelfcast.common.O;

public class LinkPagedFeed implements Link{

    private final O<LinkSimple> next;
    private final O<LinkSimple> first;
    private final O<LinkSimple> last;

    public LinkPagedFeed(O<LinkSimple> next, O<LinkSimple> first, O<LinkSimple> last) {
        this.next = next;
        this.first = first;
        this.last = last;
    }

    public O<LinkSimple> getNext() {
        return next;
    }

    public O<LinkSimple> getFirst() {
        return first;
    }

    public O<LinkSimple> getLast() {
        return last;
    }
}
