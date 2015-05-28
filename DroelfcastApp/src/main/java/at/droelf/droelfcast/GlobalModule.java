package at.droelf.droelfcast;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

import at.droelf.droelfcast.common.Logger;
import at.droelf.droelfcast.dagger.ScopeSingleton;
import dagger.Module;
import dagger.Provides;

@Module
final class GlobalModule {

    private final Application application;

    GlobalModule(Application application){
        this.application = application;
    }

    @Provides
    Application application(){
        return application;
    }

    @Provides
    Context context(){
        return application.getApplicationContext();
    }

    @Provides
    GsonParceler provideGsonParceler(Gson gson){
        return new GsonParceler(gson);
    }

    @Provides
    Gson provideGson(){
        return new Gson();
    }

    @Provides
    Logger provideLogger(){
        return Logger.INSTANCE;
    }

}
