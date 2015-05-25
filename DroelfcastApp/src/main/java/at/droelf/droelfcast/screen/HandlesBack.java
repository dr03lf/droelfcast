package at.droelf.droelfcast.screen;

public interface HandlesBack {
  /**
   * Returns <code>true</code> if back event was handled, <code>false</code> if someone higher in
   * the chain should.
   */
  boolean onBackPressed();
}