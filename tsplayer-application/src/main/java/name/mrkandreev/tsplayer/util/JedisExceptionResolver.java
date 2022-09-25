package name.mrkandreev.tsplayer.util;

import name.mrkandreev.tsplayer.exception.NotFoundException;
import name.mrkandreev.tsplayer.exception.StorageConnectionError;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisExceptionResolver {
  private JedisExceptionResolver() {
    // empty
  }

  public static <T> T throwSpecific(Exception e) {
    if ("ERR TSDB: the key does not exist".equals(e.getMessage())) {
      throw new NotFoundException();
    }

    if (e instanceof JedisConnectionException) {
      throw new StorageConnectionError((JedisConnectionException) e);
    }

    throw new RuntimeException();
  }
}
