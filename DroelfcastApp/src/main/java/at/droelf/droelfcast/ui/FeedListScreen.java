package at.droelf.droelfcast.ui;

import android.os.Bundle;

import javax.inject.Inject;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.backend.FeedService;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.flow.Layout;
import at.droelf.droelfcast.stuff.ActionBarOwner;
import at.droelf.droelfcast.stuff.InjectablePresenter;
import at.droelf.droelfcast.stuff.WithPresenter;
import flow.Flow;
import flow.path.Path;

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
            getView().initList(feedService.loadFeeds());
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
