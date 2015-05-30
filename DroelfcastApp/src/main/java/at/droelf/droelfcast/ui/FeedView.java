package at.droelf.droelfcast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.feedparser.model.parsed.item.Item;
import at.droelf.droelfcast.feedparser.model.raw.item.RawItem;
import at.droelf.droelfcast.stuff.PresenterService;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedView extends FrameLayout{

    @Inject
    FeedScreen.Presenter presenter;

    @InjectView(R.id.feed_episodes)
    ListView listView;

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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.takeView(this);
        super.onDetachedFromWindow();
    }


    public void setFeedList(final List<Item> episodes){
        final List<String> list = new ArrayList<>();

        listView.setAdapter(new ListAdapter(episodes));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Item item = ((ListAdapter) parent.getAdapter()).getItem(position);
                presenter.onEpisodeSelected(item);
            }
        });
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
