package at.droelf.droelfcast;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import javax.inject.Inject;

import at.droelf.droelfcast.dagger.DaggerService;
import at.droelf.droelfcast.dagger.scope.GlobalActivity;
import at.droelf.droelfcast.flow.GsonParceler;
import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Component;
import flow.Flow;
import flow.path.PathContainerView;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import timber.log.Timber;


public class MainActivity extends Activity implements Flow.Dispatcher {

    private MortarScope activityScope;

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

        this.activityScope = initMoartarAndDagger();

        // Mortar bundle service runner
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        // set layout and get container
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        Timber.d("----- Test: %s %s", gsonParceler, application);
    }

    private MortarScope initMoartarAndDagger(){

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


    @Override
    public Object getSystemService(String name) {
        return (activityScope != null && activityScope.hasService(name)) ? activityScope.getService(name) : super.getSystemService(name);
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


    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public void dispatch(final Flow.Traversal traversal, final Flow.TraversalCallback callback) {

    }
}
