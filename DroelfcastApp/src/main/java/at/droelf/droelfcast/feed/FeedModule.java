package at.droelf.droelfcast.feed;

import javax.inject.Singleton;

import at.droelf.droelfcast.feedparser.FeedParserService;
import dagger.Module;
import dagger.Provides;

@Module
public class FeedModule{

    @Provides
    public FeedParserService providesFeedParserService(){
        return new FeedParserService();
    }

}