package at.droelf.droelfcast.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.common.StringUtils;
import at.droelf.droelfcast.feedparser.FeedParserResponse;
import at.droelf.droelfcast.feedparser.model.parsed.Channel;
import at.droelf.droelfcast.feedparser.model.parsed.image.ImageBundle;
import at.droelf.droelfcast.feedparser.model.parsed.item.Item;
import at.droelf.droelfcast.stuff.PresenterService;
import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import rx.Subscriber;
import rx.android.schedulers.HandlerSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class FeedView extends CoordinatorLayout {

    private final FeedScreen.Presenter presenter;

    @InjectView(R.id.feed_episodes)
    RecyclerView recyclerView;

    @InjectView(R.id.backdrop)
    ImageView backDrop;

    public FeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        presenter = PresenterService.getPresenter(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);


    }

    @Override
    public void onDetachedFromWindow() {
        presenter.takeView(this);
        super.onDetachedFromWindow();
    }

    public void setBackDrop(String imageHref){
        ImageLoader.getInstance().displayImage(imageHref, backDrop);
    }


    public void setFeedList(final List<Item> episodes){
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(recyclerAdapter);


        rx.Observable.from(episodes)
                .observeOn(HandlerSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(recyclerAdapter.getSubscription());

    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private final List<Item> itemList;
        private final CompositeSubscription subscriber;


        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ViewHolder(View viewGroup) {
                super(viewGroup);
                textView = (TextView) viewGroup.findViewById(android.R.id.text1);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public RecyclerAdapter() {
            this.itemList = new ArrayList<>();
            this.subscriber = new CompositeSubscription();
        }

        public Subscriber<Item> getSubscription(){
            final Subscriber<Item> s = new Subscriber<Item>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(getContext(), "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(Item item) {
                    boolean f = false;
                    for(Item i : itemList){
                        if(i.getItemInfo().getTitle().equals(item.getItemInfo().getTitle())){
                            f = true;
                        }
                    }
                    if(!f) {
                        RecyclerAdapter.this.itemList.add(item);
                        RecyclerAdapter.this.notifyItemInserted(itemList.size() - 1);
                    }
                }
            };
            subscriber.add(s);
            return s;
        }

//        public Subscriber<FeedParserResponse> getSubscriber(){
//            return subscriber;
//        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Item item = itemList.get(position);
//            holder.mTextView.setText(mDataset[position]);
            holder.textView.setText(item.getItemInfo().getTitle());

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onEpisodeSelected(item);
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    class ListAdapter extends BaseAdapter{

        private List<Item> items;

        ListAdapter(List<Item> items){
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Item getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View convert = convertView;
            if(convert == null){
                convert = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            Item item = getItem(position);
            ((TextView)convert.findViewById(android.R.id.text1)).setText(item.getItemInfo().getTitle());
            return convert;
        }
    }

}
