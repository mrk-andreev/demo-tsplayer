package name.mrkandreev.tsplayer.dto;

import com.redislabs.redistimeseries.Aggregation;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DataRequest {
  private String requestId;

  private String key;

  private Long from;

  private Long to;

  private Aggregation aggregation;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Long getFrom() {
    return from;
  }

  public void setFrom(Long from) {
    this.from = from;
  }

  public Long getTo() {
    return to;
  }

  public void setTo(Long to) {
    this.to = to;
  }

  public Aggregation getAggregation() {
    return aggregation;
  }

  public void setAggregation(Aggregation aggregation) {
    this.aggregation = aggregation;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }
}
