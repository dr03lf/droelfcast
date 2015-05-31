package at.droelf.droelfcast.backend;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import rx.Observable;
import rx.functions.Func1;

public class FeedService {

    private FeedParserService feedParserService;
    private Context context;

    @Inject
    public FeedService(FeedParserService feedParserService, Context context){
        this.feedParserService = feedParserService;
        this.context = context;
    }

    public Observable<Channel> loadFeeds(){
        final List<String> feedList = Arrays.asList("feed.xml", "feed2.xml", "alternativlos.rss", "wrint.xml", "tal.xml");

        return Observable.from(feedList).flatMap(new Func1<String, Observable<Channel>>() {
            @Override
            public Observable<Channel> call(String s) {
                try {
                    InputStream open = context.getAssets().open(s);
                    return feedParserService.parseFeed(open);

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

        });
    }

}
