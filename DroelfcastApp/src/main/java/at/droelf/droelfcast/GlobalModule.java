package at.droelf.droelfcast;

import android.app.Application;
import android.content.Context;


import at.droelf.droelfcast.Global;
import at.droelf.droelfcast.common.Logger;
import at.droelf.droelfcast.dagger.scope.GlobalApplication;
import dagger.Module;
import dagger.Provides;

@Module
final class GlobalModule {

    private final Global global;

    GlobalModule(Global global){
        this.global = global;
    }

    @Provides
    Global global(){
        return global;
    }

    @Provides
    Context context(){
        return global.getApplicationContext();
    }

    @Provides
    @GlobalApplication(Global.GlobalComponent.class)
    Logger provideLogger(){
        return Logger.INSTANCE;
    }

}
