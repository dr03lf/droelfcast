package at.droelf.droelfcast.dagger.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;
import javax.inject.Singleton;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalApplication {
  Class<?> value();
}