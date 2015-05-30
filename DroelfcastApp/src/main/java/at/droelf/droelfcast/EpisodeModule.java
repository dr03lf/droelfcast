package at.droelf.droelfcast;

import at.droelf.droelfcast.feedparser.model.raw.item.Item;
import dagger.Module;
import dagger.Provides;

@Module
public class EpisodeModule{

    private Item mItem;

    public EpisodeModule(Item item){
        this.mItem = item;
    }

    @Provides
    public Item provideItem(){
        return mItem;
    }
}