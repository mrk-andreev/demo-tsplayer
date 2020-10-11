package name.mrkandreev.tsplayer.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import name.mrkandreev.tsplayer.service.DataCompressionService;

@Path("/schema")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SchemaResource {
  @Inject DataCompressionService dataCompressionService;

  @GET
  public String getSchema() {
    return dataCompressionService.getSchema();
  }
}
