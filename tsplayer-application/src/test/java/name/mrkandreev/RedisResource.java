package name.mrkandreev;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import org.testcontainers.containers.GenericContainer;

public class RedisResource implements QuarkusTestResourceLifecycleManager {
  private static final Integer PORT = 6379;
  private static final String CONTAINER_IMAGE = "redislabs/redistimeseries:1.4.3";

  private GenericContainer<?> container;

  @Override
  public Map<String, String> start() {
    container = new GenericContainer<>(CONTAINER_IMAGE).withExposedPorts(PORT);
    container.start();

    return Map.of("redis.port", container.getMappedPort(PORT).toString());
  }

  @Override
  public void stop() {
    container.stop();
  }
}
