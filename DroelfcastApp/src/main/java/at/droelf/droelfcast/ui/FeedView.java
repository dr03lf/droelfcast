package at.droelf.droelfcast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import at.droelf.droelfcast.MainActivity;
import at.droelf.droelfcast.R;
import at.droelf.droelfcast.dagger.DaggerService;
import at.droelf.droelfcast.feedparser.model.item.Item;
import at.droelf.droelfcast.stuff.InjectablePresenter;
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
        for(Item item : episodes){
            list.add(item.getTitle());
        }

        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                list
        );

        listView.setAdapter(stringArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onEpisodeSelected(episodes.get(position));
            }
        });
    }

}
