package at.droelf.droelfcast.stuff;

import android.content.Context;

/**
 * Mortar service to find an {@link InjectablePresenter} for a context scope.
 */
public class PresenterService {
  public static final String SERVICE_NAME = "base_presenter";

  @SuppressWarnings("unchecked")
  public static <T extends InjectablePresenter> T getPresenter(Context context) {
    return (T) context.getSystemService(SERVICE_NAME);
  }
}