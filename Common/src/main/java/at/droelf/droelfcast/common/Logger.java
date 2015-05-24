package at.droelf.droelfcast.common;

import timber.log.Timber;


public enum Logger {
    INSTANCE;

    Logger(){
        Timber.plant(new Timber.DebugTree());
    }
}
