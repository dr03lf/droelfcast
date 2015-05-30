package at.droelf.droelfcast.feedparser.model.parsed.link;

import at.droelf.droelfcast.common.O;

public class LinkPagedFeed {

    private final O<Link> next;
    private final O<Link> first;
    private final O<Link> last;

    public LinkPagedFeed(O<Link> next, O<Link> first, O<Link> last) {
        this.next = next;
        this.first = first;
        this.last = last;
    }

    public O<Link> getNext() {
        return next;
    }

    public O<Link> getFirst() {
        return first;
    }

    public O<Link> getLast() {
        return last;
    }
}
