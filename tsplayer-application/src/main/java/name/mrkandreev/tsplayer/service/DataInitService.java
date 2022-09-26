package name.mrkandreev.tsplayer.service;

import io.quarkus.runtime.Startup;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import name.mrkandreev.tsplayer.dto.SeriesInput;
import name.mrkandreev.tsplayer.dto.ValueInput;
import org.jboss.logging.Logger;

@Startup
@ApplicationScoped
public class DataInitService {
  private static final Logger LOG = Logger.getLogger(DataInitService.class);
  private static final String SERIES_NAME = "mydata";
  private static final int POINTS_COUNT = 1_000_000;
  private static final int BATCH_SIZE = 100_000;

  @Inject
  public DataInitService(DataService dataService) {
    var series = new SeriesInput();
    series.setName(SERIES_NAME);
    boolean isOk = dataService.create(series);
    if (!isOk) {
      dataService.delete(SERIES_NAME);
      dataService.create(series);
    }

    LOG.info("Start datagen");
    var currentTimestamp = System.currentTimeMillis() / 1000L;
    double y = ThreadLocalRandom.current().nextDouble();
    List<ValueInput> batch = new ArrayList<>();
    for (int i = 0; i < POINTS_COUNT; i++) {
      var value = new ValueInput();
      value.setTimestamp(currentTimestamp - i);
      y = y + ThreadLocalRandom.current().nextDouble(-10d, 10d);
      value.setValue(y);
      batch.add(value);

      if (batch.size() == BATCH_SIZE) {
        LOG.info("- upload batch");
        dataService.upload(series.getName(), batch.toArray(new ValueInput[] {}));
        batch.clear();
      }
    }
    if (!batch.isEmpty()) {
      LOG.info("- upload batch");
      dataService.upload(series.getName(), batch.toArray(new ValueInput[] {}));
    }
    LOG.info("Complete datagen");
  }
}
