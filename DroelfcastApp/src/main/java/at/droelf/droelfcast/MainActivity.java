package at.droelf.droelfcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import at.droelf.droelfcast.backend.FeedService;
import at.droelf.droelfcast.dagger.DaggerService;
import at.droelf.droelfcast.dagger.scope.GlobalActivity;
import at.droelf.droelfcast.feedparser.FeedParserService;
import at.droelf.droelfcast.flow.GsonParceler;
import at.droelf.droelfcast.flow.HandlesBack;
import at.droelf.droelfcast.stuff.ActionBarOwner;
import at.droelf.droelfcast.ui.EpisodeScreen;
import at.droelf.droelfcast.ui.FeedListScreen;
import at.droelf.droelfcast.ui.FeedScreen;
import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Component;
import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.path.Path;
import flow.path.PathContainerView;
import mortar.MortarScope;
import mortar.MortarScopeDevHelper;
import mortar.bundler.BundleServiceRunner;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;


public class MainActivity extends AppCompatActivity implements Flow.Dispatcher, ActionBarOwner.Activity {

    private MortarScope activityScope;
    private FlowDelegate flowDelegate;
    private HandlesBack containerAsBackTarget;
    private ActionBarOwner.MenuAction actionBarMenuAction;

    @InjectView(R.id.container)
    PathContainerView container;

    @Inject
    GsonParceler gsonParceler;

    @Inject
    Global application;

    @Inject
    ActionBarOwner actionBarOwner;


    @GlobalActivity(ActivityComponent.class)
    @Component(
            modules = ActivityModule.class,
            dependencies = Global.GlobalComponent.class
    )
    public interface ActivityComponent {
        void inject(MainActivity mainActivity);
        GsonParceler gsonParceler();
        FeedParserService feedParserService();
        ActionBarOwner actionBarOwner();
        FeedService feedService();

        void inject(FeedScreen.Presenter feedView);
        void inject(EpisodeScreen.Presenter episodeView);
        void inject(FeedListScreen.Presenter feedListView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activityScope = initMortarAndDagger();

        // Mortar bundle service runner
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        // actionbar
        actionBarOwner.takeView(this);

        // set layout and get container
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        containerAsBackTarget = (HandlesBack) container;

        // Init flow
        initFlow(savedInstanceState);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        flowDelegate.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        flowDelegate.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flowDelegate.onResume();
    }

    @Override
    public Object getSystemService(String name) {
        // Check if flow has the required service
        if(flowDelegate != null && flowDelegate.getSystemService(name) != null){
            return flowDelegate.getSystemService(name);
        }

        // Check if mortar has the required service
        if(activityScope != null && activityScope.hasService(name)){
            return activityScope.getService(name);
        }

        return super.getSystemService(name);
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        flowDelegate.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return flowDelegate.onRetainNonConfigurationInstance();
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
    public void dispatch(final Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        Path path = traversal.destination.top();
        setTitle(path.getClass().getSimpleName());
        boolean canGoBack = traversal.destination.size() > 1;
        String title = path.getClass().getSimpleName();
        actionBarOwner.setConfig(new ActionBarOwner.Config(false, canGoBack, title, null));

        container.dispatch(traversal, callback);
    }

    @Override
    public void onBackPressed() {
        if (containerAsBackTarget.onBackPressed()) return;
        if (flowDelegate.onBackPressed()) return;
        super.onBackPressed();
    }


    private void initFlow(Bundle savedInstanceState){
        final FlowDelegate.NonConfigurationInstance nonConfig = (FlowDelegate.NonConfigurationInstance) getLastCustomNonConfigurationInstance();
        final History history = History.single(new FeedListScreen());
        flowDelegate = FlowDelegate.onCreate(nonConfig, getIntent(), savedInstanceState, gsonParceler, history, this);
    }

    private MortarScope initMortarAndDagger(){
        final Global.GlobalComponent globalComponent = DaggerService.getDaggerComponent(getApplicationContext());
        final ActivityComponent component = DaggerService.createComponent(ActivityComponent.class, globalComponent);
        component.inject(this);

        final MortarScope parent = MortarScope.getScope(getApplicationContext());
        MortarScope scope = parent.findChild(getScopeName());
        if(scope == null){
            scope = MortarScope.buildChild(getApplicationContext())
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, component)
                    .build(getScopeName());
        }
        return scope;
    }

    private String getScopeName() {
        return getClass().getName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (actionBarMenuAction != null) {
            menu.add(actionBarMenuAction.title)
                    .setShowAsActionFlags(SHOW_AS_ACTION_ALWAYS)
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            actionBarMenuAction.action.run();
                            return true;
                        }
                    });
        }
        menu.add("Log Scope Hierarchy")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d("DemoActivity", MortarScopeDevHelper.scopeHierarchyToString(activityScope));
                        return true;
                    }
                });
        return true;
    }

    @Override
    public void setShowHomeEnabled(boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    @Override
    public void setUpButtonEnabled(boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enabled);
            actionBar.setHomeButtonEnabled(enabled);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void setMenu(ActionBarOwner.MenuAction action) {
        if (action != actionBarMenuAction) {
            actionBarMenuAction = action;
            invalidateOptionsMenu();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

}
