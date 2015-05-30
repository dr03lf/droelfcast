package at.droelf.droelfcast.stuff;

import android.content.Context;
import android.util.AttributeSet;

import at.droelf.droelfcast.R;
import at.droelf.droelfcast.flow.FramePathContainerView;
import at.droelf.droelfcast.flow.SimplePathContainer;
import flow.path.Path;

public class MortarFramePathContainerView extends FramePathContainerView {
  public MortarFramePathContainerView(Context context, AttributeSet attrs) {
    super(context, attrs, new SimplePathContainer(R.id.screen_switcher_tag, Path.contextFactory(new MortarContextFactory())));
  }
}
