package at.droelf.droelfcast.feed;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.common.Logger;
import at.droelf.droelfcast.episode.EpisodeScreen;
import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.feedparser.model.Feed;
import at.droelf.droelfcast.feedparser.model.item.Item;
import at.droelf.droelfcast.screen.Layout;
import dagger.Module;
import dagger.Provides;
import flow.Flow;
import flow.path.Path;
import mortar.ViewPresenter;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.HandlerSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Layout(R.layout.screen_feed_list)
public class FeedScreen extends Path{

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
            Timber.d("Feed Screen constructor");
        }


        public void onEpisodeSelected(Item item){
            Flow.get(getView()).set(new EpisodeScreen(item));
        }


        @Override
        protected void onLoad(final Bundle savedInstanceState) {
            Timber.d("~Lifecycle~ [activity] Presenter [method] onLoad [params] [savedInstanceState]");
            final FeedView view = getView();

            InputStream open = null;

            try {
                open = view.getContext().getAssets().open("feed.xml");
            } catch (IOException e) {
                Timber.e(e, "Error openening feed");
            }

            feedParserService.parseFeed(open)
                .subscribeOn(HandlerSchedulers.from(new Handler()))
                .subscribe(new Subscriber<Feed>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted Feed");
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Timber.d("onError Feed");
                    }

                    @Override
                    public void onNext(final Feed feed) {
                        Timber.d("onNext Feed");
                        view.initListView(feed.getChannel().getItems());
                        view.setTitle(feed.getChannel().getTitle());
                    }
                });
        }

        @Override
        protected void onSave(final Bundle outState) {
            Timber.d("~Lifecycle~ [activity] Presenter [method] onSave [params] [outState]");
        }
    }

}
