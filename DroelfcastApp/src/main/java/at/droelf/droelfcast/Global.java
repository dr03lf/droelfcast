package at.droelf.droelfcast;

import android.app.Application;

import at.droelf.droelfcast.common.Logger;


public class Global extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger instance = Logger.INSTANCE;
    }
}
