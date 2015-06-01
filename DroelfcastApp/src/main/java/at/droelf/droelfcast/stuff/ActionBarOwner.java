package at.droelf.droelfcast.stuff;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import mortar.Presenter;
import mortar.bundler.BundleService;

import static mortar.bundler.BundleService.getBundleService;

public class ActionBarOwner extends Presenter<ActionBarOwner.Activity> {
  public interface Activity {
    void setShowHomeEnabled(boolean enabled);

    void setUpButtonEnabled(boolean enabled);

    void setTitle(CharSequence title);

    void setMenu(MenuAction action);

    void setColor(Drawable drawable);

    Context getContext();
  }

  public static class Config {
    public final boolean showHomeEnabled;
    public final boolean upButtonEnabled;
    public final CharSequence title;
    public final MenuAction action;

  public final Drawable background;

    public Config(boolean showHomeEnabled, boolean upButtonEnabled, CharSequence title,
        MenuAction action, Drawable drawable) {
      this.showHomeEnabled = showHomeEnabled;
      this.upButtonEnabled = upButtonEnabled;
      this.title = title;
      this.action = action;
        this.background= drawable;
    }

    public Config withAction(MenuAction action) {
      return new Config(showHomeEnabled, upButtonEnabled, title, action, background);
    }
      public Config withDrawable(Drawable drawable) {
          return new Config(showHomeEnabled, upButtonEnabled, title, action, drawable);
      }
  }

  public static class MenuAction {
    public final CharSequence title;
    public final Runnable action;

    public MenuAction(CharSequence title, Runnable action) {
      this.title = title;
      this.action = action;
    }
  }

  private Config config;

  public ActionBarOwner() {
  }

  @Override public void onLoad(Bundle savedInstanceState) {
    if (config != null) update();
  }

  public void setConfig(Config config) {
    this.config = config;
    update();
  }

  public Config getConfig() {
    return config;
  }

  @Override protected BundleService extractBundleService(Activity activity) {
    return getBundleService(activity.getContext());
  }

  private void update() {
    if (!hasView()) return;
    Activity activity = getView();

    activity.setShowHomeEnabled(config.showHomeEnabled);
    activity.setUpButtonEnabled(config.upButtonEnabled);
    activity.setTitle(config.title);
    activity.setMenu(config.action);
      if(config.background != null) activity.setColor(config.background);
  }

}
