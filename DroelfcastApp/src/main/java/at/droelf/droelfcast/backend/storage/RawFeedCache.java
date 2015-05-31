package at.droelf.droelfcast.backend.storage;

import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import at.droelf.droelfcast.backend.model.FeedResponse;
import at.droelf.droelfcast.common.Hash;
import okio.BufferedSink;
import okio.Okio;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

public class RawFeedCache {

    public Observable<FeedResponse> cacheDownloadedFeed(final String baseDir, final String url, final Response response){
        return Observable.create(new Observable.OnSubscribe<FeedResponse>() {
            @Override
            public void call(Subscriber<? super FeedResponse> subscriber) {

                final File path = new File(baseDir + File.separator + Hash.md5(url));
                if(!path.exists()){
                    final boolean mkdirs = path.mkdirs();
                    if(!mkdirs){
                        final IOException ioException = new IOException("Error creating feed directory");
                        Timber.e(ioException, "Error creating feed cache dirs");
                        subscriber.onError(ioException);
                    }
                }

                final File file = new File(path, String.valueOf(System.currentTimeMillis()));

                final BufferedSink buffer;
                try {

                    buffer = Okio.buffer(Okio.sink(file));
                    buffer.writeAll(response.body().source());
                    buffer.flush();
                    buffer.close();
                    Timber.d("Feed successfully stored in cache: %s", file.getAbsolutePath());

                    subscriber.onNext(new FeedResponse(file, url));
                    subscriber.onCompleted();

                } catch (IOException e) {
                    Timber.e(e, "Problem during saving feed");
                    subscriber.onError(e);

                }
            }
        });
    }



}
