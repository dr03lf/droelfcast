package at.droelf.droelfcast.episode;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import at.droelf.droelfcast.DaggerService;
import at.droelf.droelfcast.MainActivity;
import at.droelf.droelfcast.R;
import at.droelf.droelfcast.feed.FeedScreen;
import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class EpisodeView extends LinearLayout {

    @Inject
    EpisodeScreen.Presenter presenter;

    @InjectView(R.id.episode_text)
    TextView episodeText;

    public EpisodeView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        DaggerService.<EpisodeScreen.Component>getDaggerComponent(context).inject(this);
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

    public void setText(String text){
        episodeText.setText(text);
    }
}
