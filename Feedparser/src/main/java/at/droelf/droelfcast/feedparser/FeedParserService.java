package at.droelf.droelfcast.feedparser;

import org.simpleframework.xml.core.Persister;

import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.droelf.droelfcast.feedparser.model.converter.ChannelConverter;
import at.droelf.droelfcast.feedparser.model.converter.ChannelInfoConverter;
import at.droelf.droelfcast.feedparser.model.converter.ImageConverter;
import at.droelf.droelfcast.feedparser.model.converter.ItemConverter;
import at.droelf.droelfcast.feedparser.model.converter.ItemInfoConverter;
import at.droelf.droelfcast.feedparser.model.converter.LinkConverter;
import at.droelf.droelfcast.feedparser.model.converter.MediaInfoConverter;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.feedparser.model.raw.Feed;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

public class FeedParserService {

    @Inject Persister persister;
    @Inject ChannelConverter channelConverter;

    @dagger.Component(modules = Module.class)
    @Singleton
    public interface Component {
        void inject(FeedParserService feedParserService);
        Persister persister();
        ChannelConverter channelconverter();
    }


    public FeedParserService(){
        final Component build = DaggerFeedParserService_Component.builder()
                .module(new Module())
                .build();
        build.inject(this);
    }

    public Observable<Channel> parseFeed(final InputStream inputStream) {
        return Observable.create(new Observable.OnSubscribe<Channel>() {
            @Override
            public void call(Subscriber<? super Channel> subscriber) {

                try {
                    final Feed feed = persister.read(Feed.class, inputStream);
                    final Channel channel = channelConverter.getChannel(feed.getChannel());
                    subscriber.onNext(channel);

                } catch (Exception e) {
                    Timber.e(e, "Failure parsing xml");
                    e.printStackTrace();
                    subscriber.onError(e);

                }

            }
        });
    }


}
