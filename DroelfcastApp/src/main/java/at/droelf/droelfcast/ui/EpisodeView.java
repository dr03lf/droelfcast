package at.droelf.droelfcast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import at.droelf.droelfcast.MainActivity;
import at.droelf.droelfcast.R;
import at.droelf.droelfcast.dagger.DaggerService;
import at.droelf.droelfcast.stuff.PresenterService;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class EpisodeView extends FrameLayout{

    EpisodeScreen.Presenter presenter;

    @InjectView(R.id.episode_text)
    TextView textView;

    public EpisodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        presenter = PresenterService.getPresenter(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void setText(String string){
        textView.setText(string);
    }
}
