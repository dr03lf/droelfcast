package at.droelf.droelfcast;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import at.droelf.droelfcast.common.Logger;
import at.droelf.droelfcast.dagger.DaggerService;
import at.droelf.droelfcast.dagger.scope.GlobalApplication;
import dagger.Component;
import mortar.MortarScope;


public class Global extends Application {

    @Inject
    Logger logger;

    private MortarScope rootScope;

    @GlobalApplication(Global.GlobalComponent.class)
    @Component(modules = GlobalModule.class)
    public interface GlobalComponent{
        void inject(Global global);
        Logger logger();
        Global global();
        Context context();
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
        if(rootScope.hasService(name)){
            return rootScope.getService(name);
        }

        return super.getSystemService(name);
    }

}
