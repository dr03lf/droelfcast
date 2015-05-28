package at.droelf.droelfcast;

import android.app.Application;

import javax.inject.Inject;

import at.droelf.droelfcast.common.Logger;
import at.droelf.droelfcast.dagger.ScopeSingleton;
import dagger.Component;
import mortar.MortarScope;
import timber.log.Timber;


public class Global extends Application {

    @Inject
    Logger logger;

    private MortarScope rootScope;

    @Component(modules = GlobalModule.class)
    public interface GlobalComponent{
        void inject(Global global);
        Logger logger();
        GsonParceler gsonParceler();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final GlobalComponent component = DaggerService.createComponent(GlobalComponent.class, new GlobalModule(this));
        this.rootScope = initMortarScope(component);

        DaggerService.<GlobalComponent>getDaggerComponent(this).inject(this);
    }

    private MortarScope initMortarScope(Object service){
        return MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, service)
                .build("Root");
    }

    @Override
    public Object getSystemService(String name) {
        Object service = rootScope.getService(name);

        return (service != null) ? service : super.getSystemService(name);
    }

}
