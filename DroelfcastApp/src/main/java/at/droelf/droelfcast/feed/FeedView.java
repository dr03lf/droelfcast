package at.droelf.droelfcast.feed;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ListView;

import javax.inject.Inject;

import at.droelf.droelfcast.DaggerService;
import at.droelf.droelfcast.feedparser.FeedParserService;

public class FeedView extends ListView {

    public FeedView(final Context context) {
        super(context);
        DaggerService.<Feed.Component>getDaggerComponent(context).inject(this);
    }
}
