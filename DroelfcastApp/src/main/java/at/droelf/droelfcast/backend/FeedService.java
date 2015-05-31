package at.droelf.droelfcast.backend;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import at.droelf.droelfcast.backend.download.FeedLoader;
import at.droelf.droelfcast.backend.model.FeedResponse;
import at.droelf.droelfcast.backend.storage.*;
import at.droelf.droelfcast.backend.storage.FeedStorage;
import at.droelf.droelfcast.feedparser.FeedParserResponse;
import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import okio.Okio;
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
    public FeedService(FeedParserService feedParserService, FeedLoader feedLoader, Context context){
        this.feedParserService = feedParserService;
        this.context = context;
        this.feedLoader = feedLoader;
        this.feedStorage = new FeedStorage(new Gson(), context);
    }

    public Observable<FeedParserResponse> addFeed(String url){
        return feedLoader.loadFeed(getCacheDir(), url).flatMap(new Func1<FeedResponse, Observable<FeedParserResponse>>() {
            @Override
            public Observable<FeedParserResponse> call(FeedResponse feedResponse) {
                return feedParserService.parseFeed(feedResponse.getUrl(), feedResponse.getFile());
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

//        return Observable.from(urls).flatMap(new Func1<String, Observable<FeedResponse>>() {
//            @Override
//            public Observable<FeedResponse> call(String s) {
//                return feedLoader.loadFeed(context.getCacheDir().getAbsolutePath(), s);
//            }
//        }).flatMap(new Func1<FeedResponse, Observable<FeedParserResponse>>() {
//            @Override
//            public Observable<FeedParserResponse> call(FeedResponse file) {
//                return feedParserService.parseFeed(file.getUrl(), file.getFile());
//            }
//        });

    }



    private String getCacheDir(){
        return context.getCacheDir().getAbsolutePath();
    }

}
