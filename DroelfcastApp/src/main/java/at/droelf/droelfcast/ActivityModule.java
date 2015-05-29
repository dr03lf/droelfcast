package at.droelf.droelfcast;


import com.google.gson.Gson;

import at.droelf.droelfcast.MainActivity;
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
    public ActionBarOwner provideActoinBarOwner(){
        return new ActionBarOwner();
    }
}
