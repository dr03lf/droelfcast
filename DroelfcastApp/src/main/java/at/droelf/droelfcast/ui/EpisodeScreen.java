package at.droelf.droelfcast.ui;

import android.os.Bundle;

import javax.inject.Inject;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.feedparser.model.parsed.item.Item;
import at.droelf.droelfcast.feedparser.model.raw.item.RawItem;
import at.droelf.droelfcast.flow.Layout;
import at.droelf.droelfcast.stuff.ActionBarOwner;
import at.droelf.droelfcast.stuff.InjectablePresenter;
import at.droelf.droelfcast.stuff.WithPresenter;
import flow.path.Path;

@Layout(R.layout.screen_episode) @WithPresenter(EpisodeScreen.Presenter.class)
public class EpisodeScreen extends Path{

    private final Item item;

    public EpisodeScreen(Item item){
        this.item = item;
    }

    public class Presenter extends InjectablePresenter<EpisodeView>{

        @Inject
        ActionBarOwner actionBarOwner;

        public Presenter(PresenterInjector presenterInjector){
            super(presenterInjector);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            //actionBarOwner.setConfig(new ActionBarOwner.Config(true, true, item.getItemInfo().getTitle(), null, null));

            getView().setText(item.getItemInfo().getContent());
        }

        @Override
        protected void onSave(Bundle outState) {
            super.onSave(outState);
        }
    }
}
