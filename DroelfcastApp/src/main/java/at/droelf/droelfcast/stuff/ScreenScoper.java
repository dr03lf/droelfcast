package at.droelf.droelfcast.stuff;

import android.content.Context;

import at.droelf.droelfcast.dagger.DaggerService;
import flow.path.Path;
import mortar.MortarScope;

public class ScreenScoper {

  static final String TAG = ScreenScoper.class.getSimpleName();

  PresenterServiceFactoryProvider presenterServiceFactory = new PresenterServiceFactoryProvider();
  ComponentServiceFactoryProvider componentServiceFactory = new ComponentServiceFactoryProvider();

  /**
   * Finds or creates the scope for the given path, honoring its optional {@link
   * WithPresenterFactory} or {@link WithPresenter}, {@link WithComponent}, {@link WithComponentFactory} annotation.
   * Note that scopes are also created for unannotated screens.
   */
  public MortarScope getScreenScope(Context context, String name, Path path) {
    MortarScope parentScope = MortarScope.getScope(context);
    MortarScope childScope = parentScope.findChild(name);
    if (childScope != null) return childScope;

    MortarScope.Builder builder = parentScope.buildChild();

    ServiceFactory serviceFactory;
    serviceFactory = presenterServiceFactory.getServiceFactory(path);
    if (serviceFactory != null) {
      Object presenter = serviceFactory.getService(context, path);
      return builder.withService(PresenterService.SERVICE_NAME, presenter).build(name);
    }
    serviceFactory = componentServiceFactory.getServiceFactory(path);
    if (serviceFactory != null) {
      Object component = serviceFactory.getService(context, path);
      return builder.withService(DaggerService.SERVICE_NAME, component).build(name);
    }
    return builder.build(name);

  }

}
