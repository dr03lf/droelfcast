package at.droelf.droelfcast;


import android.content.Context;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import at.droelf.droelfcast.MainActivity;
import at.droelf.droelfcast.backend.FeedService;
import at.droelf.droelfcast.backend.download.FeedLoader;
import at.droelf.droelfcast.backend.storage.RawFeedCache;
import at.droelf.droelfcast.dagger.scope.GlobalActivity;
import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.flow.GsonParceler;
import at.droelf.droelfcast.stuff.ActionBarOwner;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    @GlobalActivity(MainActivity.ActivityComponent.class)
    GsonParceler provideGsonParceler(Gson gson){
        return new GsonParceler(gson);
    }

    @Provides
    @GlobalActivity(MainActivity.ActivityComponent.class)
    public Gson provideGson(){
        return new Gson();
    }


    @Provides
    @GlobalActivity(MainActivity.ActivityComponent.class)
    public FeedParserService provideFeedParser(){
        return new FeedParserService();
    }

    @Provides
    @GlobalActivity(MainActivity.ActivityComponent.class)
    public OkHttpClient provideOkHttpClient(){
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        return okHttpClient;
    }

    @Provides
    @GlobalActivity(MainActivity.ActivityComponent.class)
    public FeedLoader provideFeedLoader(OkHttpClient okHttpClient){
        return new FeedLoader(okHttpClient, new RawFeedCache()); //TODO
    }

    @Provides
    @GlobalActivity(MainActivity.ActivityComponent.class)
    public FeedService provideFeedService(FeedParserService feedParserService, FeedLoader feedLoader, Context context){
        return new FeedService(feedParserService, feedLoader, context);
    }

    @Provides
    @GlobalActivity(MainActivity.ActivityComponent.class)
    public ActionBarOwner provideActoinBarOwner(){
        return new ActionBarOwner();
    }
}
