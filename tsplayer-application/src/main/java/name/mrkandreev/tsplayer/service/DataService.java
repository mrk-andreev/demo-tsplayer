package name.mrkandreev.tsplayer.service;

import com.redislabs.redistimeseries.Aggregation;
import com.redislabs.redistimeseries.Measurement;
import com.redislabs.redistimeseries.RedisTimeSeries;
import com.redislabs.redistimeseries.Value;
import com.redislabs.redistimeseries.information.Info;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import name.mrkandreev.tsplayer.config.RedisConfig;
import name.mrkandreev.tsplayer.dto.SeriesInput;
import name.mrkandreev.tsplayer.dto.SeriesMetaDto;
import name.mrkandreev.tsplayer.dto.ValueInput;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@ApplicationScoped
public class DataService {
  private final RedisTimeSeries rts;

  public DataService(RedisConfig redisConfig) {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setJmxEnabled(false);

    rts =
        new RedisTimeSeries(
            new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort()));
  }

  public Boolean create(SeriesInput input) {
    return rts.create(input.getName());
  }

  public Value[] fetch(String key, Long from, Long to, Aggregation aggregation, Long timeBucket) {
    return rts.range(key, from, to, aggregation, timeBucket);
  }

  public void upload(String key, ValueInput... values) {
    rts.madd(
        Stream.of(values)
            .map(value -> new Measurement(key, value.getTimestamp(), value.getValue()))
            .toArray(Measurement[]::new));
  }

  public SeriesMetaDto getMeta(String key) {
    Info info = rts.info(key);
    return new SeriesMetaDto(info.getProperty("firstTimestamp"), info.getProperty("lastTimestamp"));
  }
}
