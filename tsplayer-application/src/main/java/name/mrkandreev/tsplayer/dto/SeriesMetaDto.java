package name.mrkandreev.tsplayer.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SeriesMetaDto {
  private Long minTimestamp;

  private Long maxTimestamp;

  public SeriesMetaDto(Long minTimestamp, Long maxTimestamp) {
    this.minTimestamp = minTimestamp;
    this.maxTimestamp = maxTimestamp;
  }

  public Long getMinTimestamp() {
    return minTimestamp;
  }

  public void setMinTimestamp(Long minTimestamp) {
    this.minTimestamp = minTimestamp;
  }

  public Long getMaxTimestamp() {
    return maxTimestamp;
  }

  public void setMaxTimestamp(Long maxTimestamp) {
    this.maxTimestamp = maxTimestamp;
  }
}
