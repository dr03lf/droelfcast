package at.droelf.droelfcast.feed;

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