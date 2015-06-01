package at.droelf.droelfcast.backend;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import at.droelf.droelfcast.backend.download.FeedLoader;
import at.droelf.droelfcast.backend.model.FeedResponse;
import at.droelf.droelfcast.backend.storage.FeedStorage;
import at.droelf.droelfcast.feedparser.FeedParserResponse;
import at.droelf.droelfcast.feedparser.FeedParserService;
import rx.Observable;
import rx.functions.Func1;

public class FeedService {

    private FeedParserService feedParserService;
    private FeedLoader feedLoader;
    private Context context;

    private FeedStorage feedStorage;

    final List<String> urls = Arrays.asList(
            "http://freakshow.fm/feed/mp3/",
            "http://cre.fm/feed/mp3/",
            "http://alternativlos.org/alternativlos.rss",
            "http://feeds.thisamericanlife.org/talpodcast",
            "http://www.wrint.de/category/podcast_alles_kurzfeed/feed/"
    );

    @Inject
    public FeedService(FeedParserService feedParserService, FeedLoader feedLoader, Context context, FeedStorage feedStorage){
        this.feedParserService = feedParserService;
        this.context = context;
        this.feedLoader = feedLoader;
        this.feedStorage = feedStorage;
    }

    public Observable<FeedParserResponse> addFeed(String url){
        return feedLoader.loadAndCacheRawFeed(url).flatMap(new Func1<FeedResponse, Observable<FeedParserResponse>>() {
            @Override
            public Observable<FeedParserResponse> call(FeedResponse feedResponse) {
                feedResponse.getCacheKey();

                try {
                    final InputStream cachedRawFeed = feedLoader.getCachedRawfeed(feedResponse.getCacheKey());
                    return feedParserService.parseFeed(feedResponse.getUrl(), cachedRawFeed);

                } catch (IOException e) {
                    e.printStackTrace();
                    return Observable.error(e);
                }

            }
        }).flatMap(new Func1<FeedParserResponse, Observable<FeedParserResponse>>() {
            @Override
            public Observable<FeedParserResponse> call(FeedParserResponse feedParserResponse) {
                return feedStorage.saveChannel(feedParserResponse);
            }
        });
    }

    public Observable<FeedParserResponse> loadFeeds(){
        return feedStorage.loadAllChannels();
    }



}
