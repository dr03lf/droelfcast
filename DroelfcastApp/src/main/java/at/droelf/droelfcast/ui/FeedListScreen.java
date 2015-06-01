package at.droelf.droelfcast.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.backend.FeedService;
import at.droelf.droelfcast.feedparser.FeedParserResponse;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.flow.Layout;
import at.droelf.droelfcast.stuff.ActionBarOwner;
import at.droelf.droelfcast.stuff.InjectablePresenter;
import at.droelf.droelfcast.stuff.WithPresenter;
import flow.Flow;
import flow.path.Path;
import rx.Observable;
import rx.android.schedulers.HandlerSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Layout(R.layout.screen_feed_list) @WithPresenter(FeedListScreen.Presenter.class)
public class FeedListScreen extends Path {

    public static class Presenter extends InjectablePresenter<FeedListView> {

        @Inject
        FeedService feedService;

        @Inject
        ActionBarOwner actionBarOwner;

        public Presenter(PresenterInjector injector) {
            super(injector);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            final FeedListView view = getView();
            view.initList();

            final List<String> urls = Arrays.asList(
                    "http://freakshow.fm/feed/mp3/",
                    "http://cre.fm/feed/mp3/",
                    "http://alternativlos.org/alternativlos.rss",
                    "http://feeds.thisamericanlife.org/talpodcast",
                    "http://www.wrint.de/category/podcast_alles_kurzfeed/feed/"
            );

            feedService.loadFeeds()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(HandlerSchedulers.mainThread())
                    .subscribe(view.getSubscriber());

            ActionBarOwner.MenuAction add = new ActionBarOwner.MenuAction("add", new Runnable() {
                @Override
                public void run() {
                    Observable.from(urls).flatMap(new Func1<String, Observable<FeedParserResponse>>() {
                        @Override
                        public Observable<FeedParserResponse> call(String s) {
                            return feedService.addFeed(s);
                        }
                    }).subscribeOn(Schedulers.newThread())
                            .observeOn(HandlerSchedulers.mainThread())
                            .subscribe(view.getSubscriber());
                }
            });

//            ActionBarOwner.Config config = new ActionBarOwner.Config(R.id.feed_list_toolbar, false, false, "Feeds", add, null);
//            if (config == null) {
//                config =
//            }

            ActionBarOwner.Config config = actionBarOwner.getConfig()
                    .withToolBarId(R.id.feed_list_toolbar)
                    .withDrawable(new ColorDrawable(android.support.v7.appcompat.R.color.material_deep_teal_500))
                    .withAction(add);

            actionBarOwner.setConfig(config);

        }

        @Override
        protected void onSave(Bundle outState) {
            super.onSave(outState);
        }

        public void onChannelSelected(Channel channel){
            Flow.get(getView()).set(new FeedScreen(channel));
        }
    }

}
