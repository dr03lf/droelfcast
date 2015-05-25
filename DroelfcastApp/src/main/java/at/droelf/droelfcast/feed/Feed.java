package at.droelf.droelfcast.feed;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.droelf.droelfcast.common.Logger;
import at.droelf.droelfcast.feedparser.FeedParserService;
import dagger.Module;
import dagger.Provides;
import mortar.ViewPresenter;
import timber.log.Timber;

public class Feed {

    @dagger.Component(modules = FeedModule.class) @Singleton
    public interface Component {
        void inject(FeedView t);
        FeedParserService feedParserService();
    }



    @Singleton
    static class Presenter extends ViewPresenter<FeedView> {

        private FeedParserService feedParserService;

        @Inject
        Presenter(FeedParserService feedParserService){
            this.feedParserService = feedParserService;
        }

        @Override
        protected void onLoad(final Bundle savedInstanceState) {
            Timber.d("~Lifecycle~", "[activity] Presenter [method] onLoad [params] [savedInstanceState]");
            final FeedView view = getView();
        }

        @Override
        protected void onSave(final Bundle outState) {
            Timber.d("~Lifecycle~", "[activity] Presenter [method] onSave [params] [outState]");
        }
    }

}
