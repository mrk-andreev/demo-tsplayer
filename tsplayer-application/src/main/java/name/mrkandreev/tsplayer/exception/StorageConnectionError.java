package name.mrkandreev.tsplayer.exception;

public class StorageConnectionError extends RuntimeException {
  public StorageConnectionError(RuntimeException e) {
    super(e);
  }
}

