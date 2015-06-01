package at.droelf.droelfcast.backend.storage;

import android.content.Context;

import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import at.droelf.droelfcast.backend.model.FeedResponse;
import at.droelf.droelfcast.common.Hash;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

import com.jakewharton.disklrucache.DiskLruCache;


public class RawFeedCache {

    private final static String FEED_CACHE_DIR = "rawfeedCache";
    private final DiskLruCache cache;

    private final static Long CACHE_SIZE = 100000000l;

    public RawFeedCache(Context context) throws IOException {
        final File fileDir = new File(context.getCacheDir() + File.separator + FEED_CACHE_DIR + File.separator);

        if(!fileDir.exists()){
            final boolean mkdirs = fileDir.mkdirs();
            if(!mkdirs){
                IOException ioException = new IOException("Failed to create dir: " + fileDir.getAbsolutePath());
                Timber.e(ioException, "Error creating feed cache dirs");
                throw ioException;
            }
        }
        cache = DiskLruCache.open(fileDir, 1, 1, CACHE_SIZE);
    }

    public InputStream getCachedRawfeed(String key) throws IOException {
        synchronized (cache){
            return cache.get(key).getInputStream(0);
        }
    }

    public Observable<FeedResponse> cacheDownloadedFeed(final String url, final Response response){
        return Observable.create(new Observable.OnSubscribe<FeedResponse>() {
            @Override
            public void call(Subscriber<? super FeedResponse> subscriber) {

                final String key = Hash.md5(url) + "_" + System.currentTimeMillis();

                try {
                    synchronized (cache){
                        final DiskLruCache.Editor edit = cache.edit(key);
                        edit.set(0, response.body().string());
                        edit.commit();
                    }

                    subscriber.onNext(new FeedResponse(key, url));
                    subscriber.onCompleted();

                } catch (IOException e) {
                    Timber.e(e, "Problem during saving feed");
                    subscriber.onError(e);

                }
            }
        });
    }



}
