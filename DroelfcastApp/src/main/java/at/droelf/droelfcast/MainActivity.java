package at.droelf.droelfcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;

import at.droelf.droelfcast.feed.DaggerFeedScreen_Component;
import at.droelf.droelfcast.feed.FeedScreen;
import at.droelf.droelfcast.feed.FeedModule;
import at.droelf.droelfcast.screen.HandlesBack;
import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.path.Path;
import flow.path.PathContainerView;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;


public class MainActivity extends Activity implements Flow.Dispatcher {

    private FlowDelegate flowDelegate;
    private PathContainerView container;
    private HandlesBack containerAsHandlesBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mortar bundle service runner
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        // set layout and get container
        setContentView(R.layout.activity_main);
        container = (PathContainerView) findViewById(R.id.container);


        // Init flow
        @SuppressWarnings("deprecation")
        final FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance) getLastNonConfigurationInstance();

        final GsonParceler parceler = new GsonParceler(new Gson());
        flowDelegate = FlowDelegate.onCreate(
            nonConfig,
            getIntent(),
            savedInstanceState,
            parceler,
            History.single(new FeedScreen()),
            this
        );
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        flowDelegate.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flowDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flowDelegate.onPause();
    }

    @SuppressWarnings("deprecation") // https://code.google.com/p/android/issues/detail?id=151346 //TODO
    @Override
    public Object onRetainNonConfigurationInstance() {
        return flowDelegate.onRetainNonConfigurationInstance();
    }


    @Override
    public Object getSystemService(String name) {
        // Init mortar
        MortarScope scope = MortarScope.findChild(getApplicationContext(), getScopeName());
        if(scope == null){

            final FeedScreen.Component build = DaggerFeedScreen_Component.builder().feedModule(new FeedModule()).build();
            scope = MortarScope.buildChild(getApplicationContext())
                    .withService(DaggerService.SERVICE_NAME, build)
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .build(getScopeName());
        }

        // Init flow
        if(flowDelegate != null){
            Object systemService = flowDelegate.getSystemService(name);
            if(systemService != null) return systemService;
        }

        return scope.hasService(name) ? scope.getService(name) : super.getSystemService(name);
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        flowDelegate.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if(!containerAsHandlesBack.onBackPressed()) super.onBackPressed();
    }



    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            MortarScope activityScope = MortarScope.findChild(getApplicationContext(), getScopeName());
            if (activityScope != null) activityScope.destroy();
        }

        super.onDestroy();
    }


    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public void dispatch(final Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        final Path newScreen = traversal.destination.top();
        final String title = newScreen.getClass().getSimpleName();

        //AB stuff

        container.dispatch(traversal, callback);

    }
}
