package at.droelf.droelfcast.feedparser.model.converter;

import java.util.ArrayList;
import java.util.List;

import at.droelf.droelfcast.common.CollectionUtils;
import at.droelf.droelfcast.common.O;
import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.model.parsed.link.Link;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkAlternateFeed;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkPagedFeed;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkType;
import at.droelf.droelfcast.feedparser.model.raw.RawLink;

public class LinkConverter {

    public List<LinkAlternateFeed> getAlternateFeedLinks(List<RawLink> rawLinks){
        final List<LinkAlternateFeed> linkAlternateFeeds = new ArrayList<>();
        final List<RawLink> linkByType = getLinkByType(rawLinks, LinkType.ALTERNATE);

        for(RawLink rawLink : linkByType){
            linkAlternateFeeds.add(new LinkAlternateFeed(
                    StringUtils.ensureEmpty(rawLink.getHref()),
                    StringUtils.ensureEmpty(rawLink.getTitle()),
                    StringUtils.ensureEmpty(rawLink.getType())
            ));
        }

        return linkAlternateFeeds;
    }


    public O<LinkPagedFeed> getLinkPagedFeed(List<RawLink> rawLinks) {

        final O<RawLink> first = CollectionUtils.getFirstElement(getLinkByType(rawLinks, LinkType.PAGED_FIRST));
        final O<RawLink> last = CollectionUtils.getFirstElement(getLinkByType(rawLinks, LinkType.PAGED_LAST));
        final O<RawLink> next = CollectionUtils.getFirstElement(getLinkByType(rawLinks, LinkType.PAGED_NEXT));

        if(first == null && last == null && next == null){
            return O.n();
        }

        final O<Link> nullLink = O.n();

        return O.c(new LinkPagedFeed(
                next.e() ? nullLink : O.c(new Link(LinkType.PAGED_NEXT, next.d().getHref())),
                first.e() ? nullLink : O.c(new Link(LinkType.PAGED_FIRST, first.d().getHref())),
                last.e() ? nullLink : O.c(new Link(LinkType.PAGED_LAST, last.d().getHref()))
        ));
    }


    private List<RawLink> getLinkByType(List<RawLink> rawLinks, LinkType linkType){
        final List<RawLink> sortedLinks = new ArrayList<>();

        for(RawLink rawLink : rawLinks){
            if(rawLink.getRel() == null && linkType.getRel() == null){
                sortedLinks.add(rawLink);

            } else if(rawLink.getRel() != null && linkType.getRel() != null &&
                    rawLink.getRel().equals(linkType.getRel())){
                sortedLinks.add(rawLink);
            }
        }

        return sortedLinks;
    }

}
