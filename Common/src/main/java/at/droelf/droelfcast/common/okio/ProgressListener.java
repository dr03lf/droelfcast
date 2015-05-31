package at.droelf.droelfcast.common.okio;

public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}