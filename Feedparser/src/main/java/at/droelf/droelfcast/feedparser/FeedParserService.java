package at.droelf.droelfcast.feedparser;

import org.simpleframework.xml.core.Persister;

import java.io.InputStream;

import at.droelf.droelfcast.feedparser.model.Feed;
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
