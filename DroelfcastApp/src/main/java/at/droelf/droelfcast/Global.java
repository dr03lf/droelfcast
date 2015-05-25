package at.droelf.droelfcast;

import android.app.Application;

import at.droelf.droelfcast.common.Logger;
import at.droelf.droelfcast.feedparser.FeedParserService;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import mortar.MortarScope;
import timber.log.Timber;


public class Global extends Application {

    private MortarScope rootScope;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger instance = Logger.INSTANCE;
        Timber.d("onCreate");
    }

    @Override
    public Object getSystemService(String name) {
        if (rootScope == null) rootScope = MortarScope.buildRootScope().build("Root");

        return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
    }

}
