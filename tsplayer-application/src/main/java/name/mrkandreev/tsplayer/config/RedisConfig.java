package name.mrkandreev.tsplayer.config;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "redis")
public class RedisConfig {
  private String host;

  private Integer port;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }
}
