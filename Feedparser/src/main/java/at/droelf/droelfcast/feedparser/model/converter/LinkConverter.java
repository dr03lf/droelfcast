package at.droelf.droelfcast.feedparser.model.converter;

import java.util.ArrayList;
import java.util.List;

import at.droelf.droelfcast.common.CollectionUtils;
import at.droelf.droelfcast.common.O;
import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkBundle;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkPayment;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkRss;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkSimple;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkAlternateFeed;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkPagedFeed;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkType;
import at.droelf.droelfcast.feedparser.model.raw.RawLink;

public class LinkConverter {

    public LinkBundle getLinkBundle(List<RawLink> rawLinks){
        final List<LinkAlternateFeed> alternateFeedLinks = getAlternateFeedLinks(rawLinks);
        final O<LinkPayment> linkPayment = getLinkPayment(rawLinks);
        final O<LinkRss> linkRss = getLinkRss(rawLinks);
        final O<LinkPagedFeed> linkPagedFeed = getLinkPagedFeed(rawLinks);

        return new LinkBundle(alternateFeedLinks, linkPagedFeed, linkRss, linkPayment);
    }

    private List<LinkAlternateFeed> getAlternateFeedLinks(List<RawLink> rawLinks){
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


    private O<LinkPagedFeed> getLinkPagedFeed(List<RawLink> rawLinks) {

        final O<RawLink> first = CollectionUtils.getFirstElement(getLinkByType(rawLinks, LinkType.PAGED_FIRST));
        final O<RawLink> last = CollectionUtils.getFirstElement(getLinkByType(rawLinks, LinkType.PAGED_LAST));
        final O<RawLink> next = CollectionUtils.getFirstElement(getLinkByType(rawLinks, LinkType.PAGED_NEXT));

        if(O.allEmpty(first, last, next)){
            return O.n();
        }

        final O<LinkSimple> nullLink = O.n();

        return O.c(new LinkPagedFeed(
                next.e() ? nullLink : O.c(new LinkSimple(LinkType.PAGED_NEXT, next.d().getHref())),
                first.e() ? nullLink : O.c(new LinkSimple(LinkType.PAGED_FIRST, first.d().getHref())),
                last.e() ? nullLink : O.c(new LinkSimple(LinkType.PAGED_LAST, last.d().getHref()))
        ));
    }

    private O<LinkRss> getLinkRss(List<RawLink> rawLinks){
        final O<RawLink> rssRawLink = CollectionUtils.getFirstElement(getLinkByType(rawLinks, LinkType.RSS_LINK));
        if(!rssRawLink.e()){
            return O.c(new LinkRss(rssRawLink.d().getLink()));
        }
        return O.n();
    }

    private O<LinkPayment> getLinkPayment(List<RawLink> rawLinks){
        final O<RawLink> paymentRawLink = CollectionUtils.getFirstElement(getLinkByType(rawLinks, LinkType.PAYMENT));

        if(!paymentRawLink.e()){
            final RawLink d = paymentRawLink.d();
            return O.c(new LinkPayment(d.getHref(), d.getTitle(), d.getType()));
        }

        return O.n();
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
