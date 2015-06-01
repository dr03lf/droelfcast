package at.droelf.droelfcast.backend.storage;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.disklrucache.DiskLruCache;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import at.droelf.droelfcast.common.Hash;
import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.FeedParserResponse;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.feedparser.model.parsed.ChannelInfo;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageBundle;
import at.droelf.droelfcast.feedparser.model.parsed.item.Item;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkBundle;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

public class FeedStorage {


    private static final int FILE_CHANNEL_INFO = 0; //"channelinfo";
    private static final int FILE_LINKBUNDLE = 1; //"linkbundle";
    private static final int FILE_IMAGEBUNDLE = 2; //"imagebundle";
    private static final int FILE_ITEMS = 3;//"items";
    private static final int FILE_URL = 4; //"url";

    private final static String META_FEED_LIST = "feedlist";

    private final static Long CACHE_SIZE = FileUtils.ONE_MB * 100;


    private Gson gson;
    private Context context;
    private DiskLruCache feedCache;
    private DiskLruCache metaDataFeedCache;


    public FeedStorage(Gson gson, Context context) throws IOException {
        this.gson = gson;
        this.context = context;

        final File fileDir = getFeedBaseDir();
        final File metaDir = getMetaFeedBaseDir();
        at.droelf.droelfcast.common.FileUtils.createFileDir(fileDir);
        at.droelf.droelfcast.common.FileUtils.createFileDir(metaDir);

        this.feedCache = DiskLruCache.open(fileDir, 1, 5, CACHE_SIZE);
        this.metaDataFeedCache = DiskLruCache.open(metaDir, 1, 1, CACHE_SIZE);
    }

    public Observable<FeedParserResponse> loadAllChannels(){
        return Observable.create(new Observable.OnSubscribe<FeedParserResponse>() {
            @Override
            public void call(Subscriber<? super FeedParserResponse> subscriber) {


                try {

                    final Type listType = new TypeToken<List<String>>() {}.getType();
                    DiskLruCache.Snapshot snapshot1 = metaDataFeedCache.get(META_FEED_LIST);
                    final List<String> feedList = new ArrayList<String>();
                    if(snapshot1 != null){
                        final List<String> feeds = gson.fromJson(snapshot1.getString(0), listType);
                        feedList.addAll(feeds);
                    }


                    for(String key : feedList){

                        final DiskLruCache.Snapshot snapshot = feedCache.get(key);
                        final ChannelInfo channelInfo = gson.fromJson(snapshot.getString(FILE_CHANNEL_INFO), ChannelInfo.class);
                        final LinkBundle linkBundle = gson.fromJson(snapshot.getString(FILE_LINKBUNDLE), LinkBundle.class);
                        final ImageBundle imageBundle = gson.fromJson(snapshot.getString(FILE_IMAGEBUNDLE), ImageBundle.class);

                        final Type type = new TypeToken<List<Item>>() {}.getType();
                        final List<Item> itemList = gson.fromJson(snapshot.getString(FILE_ITEMS), type);
                        final String url = gson.fromJson(snapshot.getString(FILE_URL), String.class);

                        final FeedParserResponse feedParserResponse = new FeedParserResponse(
                                url,
                                new Channel(channelInfo, linkBundle, imageBundle, itemList)
                        );

                        if(!subscriber.isUnsubscribed()) subscriber.onNext(feedParserResponse);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    if(!subscriber.isUnsubscribed()) subscriber.onError(e);
                }
                if(!subscriber.isUnsubscribed()) subscriber.onCompleted();

            }
        });
    }

    public Observable<FeedParserResponse> saveChannel(final FeedParserResponse feedParserResponse){
        return Observable.create(new Observable.OnSubscribe<FeedParserResponse>() {
            @Override
            public void call(Subscriber<? super FeedParserResponse> subscriber) {

                final Channel channel = feedParserResponse.getChannel();
                final String key = getDirectory(feedParserResponse);

                DiskLruCache.Editor editor = null;
                DiskLruCache.Editor metaEditor = null;
                try {
                    final Type listType = new TypeToken<List<String>>() {}.getType();
                    final DiskLruCache.Snapshot snapshot = metaDataFeedCache.get(META_FEED_LIST);
                    final List<String> feedList = new ArrayList<>();
                    if(snapshot != null){
                        final List<String> feeds = gson.fromJson(snapshot.getString(0), listType);
                        feedList.addAll(feeds);
                    }
                    if(feedList.contains(key)){
                        subscriber.onError(new RuntimeException("Already added"));
                    }


                    editor = feedCache.edit(key);
                    editor.set(FILE_CHANNEL_INFO, gson.toJson(channel.getChannelInfo()));
                    editor.set(FILE_IMAGEBUNDLE, gson.toJson(channel.getImageBundle()));
                    editor.set(FILE_LINKBUNDLE, gson.toJson(channel.getLinkBundle()));
                    editor.set(FILE_ITEMS, gson.toJson(channel.getItems()));
                    editor.set(FILE_URL, gson.toJson(feedParserResponse.getUrl()));
                    editor.commit();

                    feedList.add(key);
                    metaEditor = metaDataFeedCache.edit(META_FEED_LIST);
                    metaEditor.set(0, gson.toJson(feedList));
                    metaEditor.commit();

                    if(!subscriber.isUnsubscribed()) subscriber.onNext(feedParserResponse);
                    if(!subscriber.isUnsubscribed()) subscriber.onCompleted();

                } catch (IOException e) {
                    Timber.e(e, "Error writing data :( - Try to clean up");
                    try {
                        if(editor != null){
                            editor.abort();
                        }
                        if(metaEditor != null){
                            metaEditor.abort();
                        }
                        if(!subscriber.isUnsubscribed()) subscriber.onError(e);

                    } catch (IOException e1) {
                        Timber.e(e1, "Not able to clean up ... everything is dump :(");
                        if(!subscriber.isUnsubscribed()) subscriber.onError(e1);
                    }
                    e.printStackTrace();
                }
            }
        });
    }


    private File getFeedBaseDir(){
        final File absoluteFile = context.getFilesDir().getAbsoluteFile();
        final File file = new File(absoluteFile.getAbsolutePath() + File.separator + "feed" + File.separator);
        return file;
    }

    private File getMetaFeedBaseDir(){
        final File absoluteFile = context.getFilesDir().getAbsoluteFile();
        final File file = new File(absoluteFile.getAbsolutePath() + File.separator + "metafeed" + File.separator);
        return file;
    }


    private String getDirectory(FeedParserResponse feedParserResponse){
        final String folder = feedParserResponse.getChannel().getChannelInfo().getTitle().toLowerCase();
        return Hash.md5(folder + "_" + feedParserResponse.getUrl());
    }

}
