package at.droelf.droelfcast.ui;

import android.os.Bundle;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.feedparser.model.item.Item;
import at.droelf.droelfcast.flow.Layout;
import at.droelf.droelfcast.stuff.InjectablePresenter;
import at.droelf.droelfcast.stuff.WithPresenter;

@Layout(R.layout.screen_episode) @WithPresenter(EpisodeScreen.Presenter.class)
public class EpisodeScreen {

    private final Item item;

    public EpisodeScreen(Item item){
        this.item = item;
    }

    public class Presenter extends InjectablePresenter<EpisodeView>{
        public Presenter(PresenterInjector presenterInjector){
            super(presenterInjector);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().setText(item.getDescription());
        }
    }
}
