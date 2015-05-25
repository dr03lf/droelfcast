package at.droelf.droelfcast.feed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import at.droelf.droelfcast.DaggerService;
import at.droelf.droelfcast.R;
import at.droelf.droelfcast.feedparser.model.item.Item;
import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class FeedView extends LinearLayout {

    @Inject
    FeedScreen.Presenter presenter;

    @InjectView(R.id.feed_title)
    TextView feedText;

    @InjectView(R.id.feed_episodes)
    ListView listView;


    public FeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerService.<FeedScreen.Component>getDaggerComponent(context).inject(this);
        Timber.d("Constructor");
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
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }

    public void setTitle(String text){
        feedText.setText(text);
    }


    public void initListView(List<Item> itemList){
        listView.setAdapter(new ListAdpater(itemList));
    }

    class ListAdpater extends BaseAdapter {

        List<Item> items;

        public ListAdpater(List<Item> items){
            this.items = items;
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Item getItem(final int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return 0;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view  = convertView;
            if(view == null){
                view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            final Item item = getItem(position);
            ((TextView)view.findViewById(android.R.id.text1)).setText(item.getTitle());
            return view;
        }
    }


}


