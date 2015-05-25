package at.droelf.droelfcast.episode;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.droelf.droelfcast.feed.FeedModule;
import at.droelf.droelfcast.feed.FeedView;
import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.feedparser.model.Feed;
import at.droelf.droelfcast.feedparser.model.item.Item;
import dagger.Provides;
import flow.Flow;
import mortar.ViewPresenter;
import rx.Subscriber;
import rx.android.schedulers.HandlerSchedulers;
import timber.log.Timber;

public class EpisodeScreen {

    private final Item item;

    public EpisodeScreen(Item item){
        this.item = item;
    }

    @dagger.Component @Singleton
    public interface Component {
        void inject(EpisodeView t);
    }

    @Singleton
    static class Presenter extends ViewPresenter<EpisodeView> {


        @Inject
        Presenter(){

        }

        @Override
        protected void onLoad(final Bundle savedInstanceState) {
            Timber.d("~Lifecycle~ [activity] Presenter [method] onLoad [params] [savedInstanceState]");
            final EpisodeView view = getView();
            view.setText(item.getTitle());
        }

        @Override
        protected void onSave(final Bundle outState) {
            Timber.d("~Lifecycle~ [activity] Presenter [method] onSave [params] [outState]");
        }
    }

}
