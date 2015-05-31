package at.droelf.droelfcast.ui;

import android.os.Bundle;


import javax.inject.Inject;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.feedparser.model.parsed.item.Item;
import at.droelf.droelfcast.flow.Layout;
import at.droelf.droelfcast.stuff.ActionBarOwner;
import at.droelf.droelfcast.stuff.InjectablePresenter;
import at.droelf.droelfcast.stuff.WithPresenter;
import flow.Flow;
import flow.path.Path;

@Layout(R.layout.screen_feed) @WithPresenter(FeedScreen.Presenter.class)
public class FeedScreen extends Path {

    private Channel channel;

    public FeedScreen(Channel channel) {
        this.channel = channel;
    }

    public class Presenter extends InjectablePresenter<FeedView> {

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
            view.setFeedList(channel.getItems());
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
