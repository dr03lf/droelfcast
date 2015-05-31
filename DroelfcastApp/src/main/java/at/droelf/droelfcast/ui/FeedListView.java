package at.droelf.droelfcast.ui;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageBundle;
import at.droelf.droelfcast.stuff.PresenterService;
import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.HandlerSchedulers;
import rx.schedulers.Schedulers;

public class FeedListView extends FrameLayout {

    @InjectView(R.id.feed_list)
    RecyclerView recyclerView;

    private final FeedListScreen.Presenter presenter;

    public FeedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        presenter = PresenterService.getPresenter(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        initRecyclerView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.takeView(this);
        super.onDetachedFromWindow();
    }


    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
    }

    public void initList(Observable<Channel> channelObservable){
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(channelObservable);
        recyclerView.setAdapter(recyclerAdapter);
    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private final Observable<Channel> channelObservable;
        private final List<Channel> channelList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public ImageView imageView;

            public ViewHolder(View viewGroup) {
                super(viewGroup);
                textView = (TextView) viewGroup.findViewById(R.id.feed_list_item_text);
                imageView = (ImageView) viewGroup.findViewById(R.id.feed_list_item_image);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public RecyclerAdapter(Observable<Channel> observable) {
            this.channelObservable = observable;
            this.channelList = new ArrayList<>();
            subScribe();
        }

        private void subScribe(){
            channelObservable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(HandlerSchedulers.mainThread())
                    .subscribe(new Subscriber<Channel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Channel channel) {
                            RecyclerAdapter.this.channelList.add(channel);
                            RecyclerAdapter.this.notifyItemInserted(channelList.size() - 1);
                        }
                    });
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ritem_feed_list, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Channel channel = channelList.get(position);
//            holder.mTextView.setText(mDataset[position]);
            holder.textView.setText(channel.getChannelInfo().getTitle() + StringUtils.LINE_SEPARATOR + channel.getChannelInfo().getShortDescription());
            ImageLoader imageLoader = ImageLoader.getInstance();

            ImageBundle imageBundle = channel.getImageBundle();
            String url = null;
            if(!imageBundle.getImageItunes().e()){
                url = imageBundle.getImageItunes().d().getHref();

            }else if(!imageBundle.getImageRss().e()){
                url = imageBundle.getImageRss().d().getUrl();
            }
            if(StringUtils.hasLength(url)) {
                imageLoader.displayImage(url, holder.imageView);
            }

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onChannelSelected(channel);
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return channelList.size();
        }
    }
}
