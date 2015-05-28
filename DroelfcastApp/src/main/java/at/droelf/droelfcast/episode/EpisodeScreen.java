package at.droelf.droelfcast.episode;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.droelf.droelfcast.feedparser.model.item.Item;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import mortar.ViewPresenter;
import timber.log.Timber;

public class EpisodeScreen {

    private final Item item;

    public EpisodeScreen(Item item){
        this.item = item;
    }

    @dagger.Component
    @Singleton
    public interface Component {
        void inject(EpisodeView t);
    }

//    @Module
//    public class EpisodeModule{
//        @Provides
//        Item getItem(){
//            return item;
//        }
//    }

    @Singleton
    static class Presenter extends ViewPresenter<EpisodeView> {

        @Inject
        Presenter(){

        }

        @Override
        protected void onLoad(final Bundle savedInstanceState) {
            Timber.d("~Lifecycle~ [activity] Presenter [method] onLoad [params] [savedInstanceState]");
            final EpisodeView view = getView();
//            view.setText(item.getTitle());
        }

        @Override
        protected void onSave(final Bundle outState) {
            Timber.d("~Lifecycle~ [activity] Presenter [method] onSave [params] [outState]");
        }
    }

}
