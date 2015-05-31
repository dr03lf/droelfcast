package at.droelf.droelfcast.backend.storage;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.droelf.droelfcast.common.Hash;
import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.FeedParserResponse;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.feedparser.model.parsed.ChannelInfo;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageBundle;
import at.droelf.droelfcast.feedparser.model.parsed.item.Item;
import at.droelf.droelfcast.feedparser.model.parsed.link.LinkBundle;
import okio.BufferedSink;
import okio.Okio;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

public class FeedStorage {

    private static final String FILE_CHANNEL_INFO = "channelinfo";
    private static final String FILE_LINKBUNDLE = "linkbundle";
    private static final String FILE_IMAGEBUNDLE = "imagebundle";
    private static final String FILE_ITEMS = "items";
    private static final String FILE_URL = "url";

    private Gson gson;
    private Context context;

    public FeedStorage(Gson gson, Context context){
        this.gson = gson;
        this.context = context;
    }

    public Observable<FeedParserResponse> loadAllChannels(){
        return Observable.create(new Observable.OnSubscribe<FeedParserResponse>() {
            @Override
            public void call(Subscriber<? super FeedParserResponse> subscriber) {
                final File baseDir = getBaseDir();
                final String[] list = baseDir.list(DirectoryFileFilter.INSTANCE);

                for(String dir : list){
                    final String feedBasePath = baseDir.getAbsolutePath() + File.separator + dir + File.separator;
                    new File(feedBasePath, FILE_CHANNEL_INFO);

                    try {
                        final ChannelInfo channelInfo = gson.fromJson(FileUtils.readFileToString(new File(feedBasePath, FILE_CHANNEL_INFO)), ChannelInfo.class);
                        final LinkBundle linkBundle = gson.fromJson(FileUtils.readFileToString(new File(feedBasePath, FILE_LINKBUNDLE)), LinkBundle.class);
                        final ImageBundle imageBundle = gson.fromJson(FileUtils.readFileToString(new File(feedBasePath, FILE_IMAGEBUNDLE)), ImageBundle.class);

                        final Type listType = new TypeToken<List<Item>>() {}.getType();
                        final List<Item> itemList = gson.fromJson(FileUtils.readFileToString(new File(feedBasePath, FILE_ITEMS)), listType);
                        final String url = gson.fromJson(FileUtils.readFileToString(new File(feedBasePath, FILE_URL)), String.class);

                        final FeedParserResponse feedParserResponse = new FeedParserResponse(
                                url,
                                new Channel(channelInfo, linkBundle, imageBundle, itemList)
                        );

                        subscriber.onNext(feedParserResponse);

                    } catch (IOException e) {
                        Timber.e(e, "A broken feed");
                    }

                }

                subscriber.onCompleted();

            }
        });
    }

    public Observable<FeedParserResponse> saveChannel(final FeedParserResponse feedParserResponse){
        return Observable.create(new Observable.OnSubscribe<FeedParserResponse>() {
            @Override
            public void call(Subscriber<? super FeedParserResponse> subscriber) {

                final Channel channel = feedParserResponse.getChannel();

                final String channelInfo = gson.toJson(channel.getChannelInfo());
                final String imageBundle = gson.toJson(channel.getImageBundle());
                final String linkBundle = gson.toJson(channel.getLinkBundle());
                final String items = gson.toJson(channel.getItems());
                final String url = gson.toJson(feedParserResponse.getUrl());

                final Map<String, String> map = new HashMap<>();
                map.put(FILE_CHANNEL_INFO, channelInfo);
                map.put(FILE_LINKBUNDLE, linkBundle);
                map.put(FILE_IMAGEBUNDLE, imageBundle);
                map.put(FILE_ITEMS, items);
                map.put(FILE_URL, url);

                final File dir = new File(getBaseDir() + File.separator + getDirectory(feedParserResponse));
                if(!dir.exists()){
                    final boolean mkdirs = dir.mkdirs();
                    if(!mkdirs){
                        final IOException ioException = new IOException("Error creating feed directory");
                        Timber.e(ioException, "Error creating feed cache dirs");
                        subscriber.onError(ioException);
                    }
                } else{
                    if(!subscriber.isUnsubscribed()){
                        subscriber.onNext(feedParserResponse);
                        subscriber.onCompleted();
                    }
                }

                try {
                    for(String key : map.keySet()){
                        final String data = map.get(key);
                        final File file = new File(dir, key);

                        final BufferedSink buffer = Okio.buffer(Okio.sink(file));
                        buffer.write(data.getBytes("UTF-8"));
                        buffer.flush();
                        buffer.close();
                    }

                    subscriber.onNext(feedParserResponse);
                    subscriber.onCompleted();

                }catch (IOException e){
                    Timber.e(e, "Error writing data :( - Try to clean up");
                    try {
                        FileUtils.deleteDirectory(dir);
                        subscriber.onError(e);
                    } catch (IOException e1) {
                        Timber.e(e1, "Can't delete dir ... everything is dump :(");
                        subscriber.onError(e1);
                    }
                }
            }
        });
    }


    private File getBaseDir(){
        final File absoluteFile = context.getFilesDir().getAbsoluteFile();
        final File file = new File(absoluteFile.getAbsolutePath() + File.separator + "feed" + File.separator);

        if(!file.exists()){
            file.mkdirs();
        }

        return file;

    }


    private String getDirectory(FeedParserResponse feedParserResponse){
        final String folder = feedParserResponse.getChannel().getChannelInfo().getTitle().toLowerCase().trim().replace(" ", "_");
        final String hash = Hash.md5(feedParserResponse.getUrl());
        return StringUtils.removeSpecialCharacters(folder) + "_" + hash;
    }

}
