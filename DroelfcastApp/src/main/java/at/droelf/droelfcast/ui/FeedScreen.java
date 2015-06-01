package at.droelf.droelfcast.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import javax.inject.Inject;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.common.O;
import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageItunes;
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


            O<ImageItunes> imageItunes = channel.getImageBundle().getImageItunes();
            if(!imageItunes.e()){

                final String href = imageItunes.d().getHref();
                ImageLoader.getInstance().loadImage(href, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                ActionBarOwner.Config config = actionBarOwner.getConfig().withDrawable(new ColorDrawable(palette.getMutedColor(Color.BLACK)));
                                actionBarOwner.setConfig(config);
                            }
                        });


                    }
                });

            }

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
