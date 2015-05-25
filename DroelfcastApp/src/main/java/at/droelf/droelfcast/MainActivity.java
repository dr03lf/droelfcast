package at.droelf.droelfcast;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;

import at.droelf.droelfcast.feed.DaggerFeed_Component;
import at.droelf.droelfcast.feed.Feed;
import at.droelf.droelfcast.feed.FeedModule;
import at.droelf.droelfcast.feed.FeedView;
import at.droelf.droelfcast.feedparser.FeedParserService;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

import static mortar.MortarScope.buildChild;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ((ViewGroup) findViewById(R.id.container)).addView(new FeedView(this));

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            MortarScope activityScope = MortarScope.findChild(getApplicationContext(), getScopeName());
            if (activityScope != null) activityScope.destroy();
        }

        super.onDestroy();
    }

    @Override
    public Object getSystemService(String name) {
        MortarScope scope = MortarScope.findChild(getApplicationContext(), getScopeName());
        if(scope == null){

            final Feed.Component build = DaggerFeed_Component.builder().feedModule(new FeedModule()).build();

            scope = MortarScope.buildChild(getApplicationContext())
                    .withService(DaggerService.SERVICE_NAME, build)
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .build(getScopeName());
        }

        return scope.hasService(name) ? scope.getService(name) : super.getSystemService(name);
    }


    public String getScopeName() {
        return getClass().getName();
    }
}
