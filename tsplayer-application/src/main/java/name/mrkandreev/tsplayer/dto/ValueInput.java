package name.mrkandreev.tsplayer.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ValueInput {
  private Long timestamp;

  private Double value;

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }
}
