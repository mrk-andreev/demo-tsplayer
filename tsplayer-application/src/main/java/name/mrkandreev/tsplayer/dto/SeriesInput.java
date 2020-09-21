package name.mrkandreev.tsplayer.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SeriesInput {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
