package at.droelf.droelfcast;

import at.droelf.droelfcast.feedparser.model.raw.item.RawItem;
import dagger.Module;
import dagger.Provides;

@Module
public class EpisodeModule{

    private RawItem mItem;

    public EpisodeModule(RawItem item){
        this.mItem = item;
    }

    @Provides
    public RawItem provideItem(){
        return mItem;
    }
}