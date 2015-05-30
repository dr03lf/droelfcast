package at.droelf.droelfcast.feedparser;

import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.List;

import at.droelf.droelfcast.common.O;
import at.droelf.droelfcast.feedparser.model.converter.LinkConverter;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkAlternateFeed;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkPagedFeed;
import at.droelf.droelfcast.feedparser.model.raw.Feed;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

public class FeedParserService {

    public Observable<Feed> parseFeed(final InputStream inputStream) {
        return Observable.create(new Observable.OnSubscribe<Feed>() {
            @Override
            public void call(Subscriber<? super Feed> subscriber) {

                try {

                    Persister persister = new Persister();
                    Feed feed = persister.read(Feed.class, inputStream);

                    LinkConverter linkConverter = new LinkConverter();
                    List<LinkAlternateFeed> alernateFeedLinks = linkConverter.getAlternateFeedLinks(feed.getChannel().getLinks());
                    O<LinkPagedFeed> linkPagedFeed = linkConverter.getLinkPagedFeed(feed.getChannel().getLinks());

                    subscriber.onNext(feed);
                    Timber.d("test test test");

                } catch (Exception e) {
                    Timber.e(e, "Failure parsing xml");
                    e.printStackTrace();
                    subscriber.onError(e);

                }

            }
        });
    }


}
