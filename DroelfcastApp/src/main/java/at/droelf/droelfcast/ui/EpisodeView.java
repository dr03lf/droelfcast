package at.droelf.droelfcast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Locale;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.stuff.PresenterService;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class EpisodeView extends FrameLayout{

    EpisodeScreen.Presenter presenter;

    @InjectView(R.id.episode_text)
    TextView textView;

    @InjectView(R.id.episode_content)
    WebView webView;

    public EpisodeView(Context context, AttributeSet attrs) {
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

    public void setText(String string){
        String metaTag = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\">";
        String css = "<style>img{max-width: 100%; width:auto; height: auto;}</style>";
        String html = "<!DOCTYPE html><html><head>%s</head><body>%s</body></html>";

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadDataWithBaseURL(null, String.format(Locale.US, html, metaTag + css, string), "text/html", "utf-8", null);
    }

}
