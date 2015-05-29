package at.droelf.droelfcast.ui;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;


import javax.inject.Inject;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.feedparser.model.Feed;
import at.droelf.droelfcast.feedparser.model.item.Item;
import at.droelf.droelfcast.flow.Layout;
import at.droelf.droelfcast.stuff.ActionBarOwner;
import at.droelf.droelfcast.stuff.InjectablePresenter;
import at.droelf.droelfcast.stuff.WithPresenter;
import flow.Flow;
import flow.path.Path;
import rx.Subscriber;
import rx.android.schedulers.HandlerSchedulers;

@Layout(R.layout.screen_feed) @WithPresenter(FeedScreen.Presenter.class)
public class FeedScreen extends Path {


    public static class Presenter extends InjectablePresenter<FeedView> {

        @Inject
        FeedParserService feedParserService;

        @Inject
        ActionBarOwner actionBarOwner;

        public Presenter(PresenterInjector presenterInjector){
            super(presenterInjector);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            final FeedView view = getView();


            InputStream inputStream = null;
            try {
                inputStream = view.getContext().getAssets().open("alternativlos.rss");
            } catch (IOException e) {
                e.printStackTrace();
            }

            feedParserService.parseFeed(inputStream)
                .observeOn(HandlerSchedulers.from(new Handler()))
                .subscribe(new Subscriber<Feed>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Feed feed) {
                        view.setFeedList(feed.getChannel().getItems());
                    }
                });

        }

        @Override
        protected void onSave(Bundle outState) {
            super.onSave(outState);
        }


        public void onEpisodeSelected(Item item){
            Flow.get(getView()).set(new EpisodeScreen(item));
        }

    }
}
