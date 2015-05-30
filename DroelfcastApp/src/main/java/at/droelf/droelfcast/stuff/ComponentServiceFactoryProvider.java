package at.droelf.droelfcast.stuff;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import at.droelf.droelfcast.dagger.DaggerService;
import dagger.Module;
import flow.path.Path;

/**
 * Provides factory for Dagger's component based on path's {@link WithComponent} annotation.
 * Component is able to be dependant on another module within parent context.
 * @see SimpleComponentFactory
 */
public class ComponentServiceFactoryProvider implements ServiceFactoryProvider<Path> {

  private final Map<Class, ServiceFactory> componentFactoryCache = new LinkedHashMap<>();

  @Override
  public ServiceFactory getServiceFactory(Path path) {
    Class<?> screenType = path.getClass();
    ServiceFactory componentFactory = componentFactoryCache.get(screenType);
    if (componentFactory != null) return componentFactory;

    WithComponent withComponent = screenType.getAnnotation(WithComponent.class);
    if (withComponent != null) {
      Class<?> componentClass = withComponent.value();
      componentFactory = new SimpleComponentFactory(componentClass);
    }

    if (componentFactory == null) {
      WithComponentFactory withComponentFactory = screenType.getAnnotation(WithComponentFactory.class);
      if (withComponentFactory != null) {
        Class<? extends ServiceFactory> mfClass = withComponentFactory.value();

        try {
          componentFactory = mfClass.newInstance();
        } catch (Exception e) {
          throw new RuntimeException(String.format("Failed to instantiate module factory %s for screen %s",
              withComponentFactory.value().getName(), path), e);
        }
      }
    }

    if (componentFactory != null) componentFactoryCache.put(screenType, componentFactory);
    return componentFactory;
  }

  private static class SimpleComponentFactory extends ServiceFactory.BaseServiceFactory {

    public SimpleComponentFactory(Class serviceClass) {
      super(serviceClass);
    }

    @Override
    public Object getService(Context context, Path screen) {
      Object depComponent = DaggerService.getDaggerComponent(context);
      Object depModule = null;
      // Find and instantiate inner module if any
      for (Class innerClass : screen.getClass().getClasses()) {
        if (Modifier.isStatic(innerClass.getModifiers())) continue;
        if (innerClass.getAnnotation(Module.class) != null) {
          try {
            Constructor constructor = innerClass.getDeclaredConstructor(screen.getClass());
            depModule = constructor.newInstance(screen);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (NoSuchMethodException e) {
            e.printStackTrace();
          } catch (InstantiationException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
          break;
        }
      }
      if (depModule == null) {
        return DaggerService.createComponent(serviceClass, depComponent);
      } else {
        return DaggerService.createComponent(serviceClass, depComponent, depModule);
      }
    }
  }


}
