package at.droelf.droelfcast.dagger;

import javax.inject.Scope;

@Scope
public @interface ScopeSingleton {
  Class<?> value();
}