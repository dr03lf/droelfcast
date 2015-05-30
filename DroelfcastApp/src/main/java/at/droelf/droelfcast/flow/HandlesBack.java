package at.droelf.droelfcast.flow;

public interface HandlesBack {
  /**
   * Returns <code>true</code> if back event was handled, <code>false</code> if someone higher in
   * the chain should.
   */
  boolean onBackPressed();
}