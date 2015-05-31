package at.droelf.droelfcast.backend.download;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import at.droelf.droelfcast.backend.model.FeedResponse;
import at.droelf.droelfcast.backend.storage.RawFeedCache;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

public class FeedLoader {

    private OkHttpClient okHttpClient;
    private RawFeedCache rawFeedCache;

    public FeedLoader(OkHttpClient okHttpClient, RawFeedCache rawFeedCache) {
        this.okHttpClient = okHttpClient;
        this.rawFeedCache = rawFeedCache;
    }

    public Observable<FeedResponse> loadFeed(final String baseDir, final String url) {

        return Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                try {
                    final Request request = new Request.Builder()
                            .url(url)
                            .build();

                    final Response response = okHttpClient.newCall(request)
                            .execute();

                    if (response.isSuccessful()) {
                        Timber.d("Feed download successful: %s", url);
                        subscriber.onNext(response);
                        subscriber.onCompleted();

                    } else {
                        final Throwable throwable = new IOException("Unexpected code " + response);
                        Timber.e(throwable, "Feed download failed");
                        subscriber.onError(throwable);

                    }

                } catch (IOException e) {
                    Timber.e(e, "Feed download failed - IOException");
                    subscriber.onError(e);

                }
                }
            }

            ).

            flatMap(new Func1<Response, Observable<FeedResponse>>() {
                @Override
                public Observable<FeedResponse> call (Response response){
                    return rawFeedCache.cacheDownloadedFeed(baseDir, url, response);
                }
            }

            );
        }
    }
