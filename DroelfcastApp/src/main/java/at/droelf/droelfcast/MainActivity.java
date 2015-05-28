package at.droelf.droelfcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import at.droelf.droelfcast.dagger.DaggerService;
import at.droelf.droelfcast.dagger.scope.GlobalActivity;
import at.droelf.droelfcast.flow.GsonParceler;
import at.droelf.droelfcast.flow.HandlesBack;
import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Component;
import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.path.PathContainerView;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;


public class MainActivity extends Activity implements Flow.Dispatcher {

    private MortarScope activityScope;
    private FlowDelegate flowDelegate;
    private HandlesBack containerAsBackTarget;

    @InjectView(R.id.container)
    PathContainerView container;

    @Inject
    GsonParceler gsonParceler;

    @Inject
    Global application;

    @GlobalActivity(ActivityComponent.class)
    @Component(
            modules = ActivityModule.class,
            dependencies = Global.GlobalComponent.class
    )
    public interface ActivityComponent {
        void inject(MainActivity mainActivity);
        GsonParceler gsonParceler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activityScope = initMortarAndDagger();

        // Mortar bundle service runner
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

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

    @SuppressWarnings("deprecation")
    @Override
    public Object onRetainNonConfigurationInstance() {
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

    }

    private void initFlow(Bundle savedInstanceState){
        @SuppressWarnings("deprecation")
        final FlowDelegate.NonConfigurationInstance nonConfig = (FlowDelegate.NonConfigurationInstance) getLastNonConfigurationInstance();
        final History history = History.emptyBuilder().build(); //TODO
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

}
