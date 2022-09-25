package name.mrkandreev.tsplayer.resource;

import com.redislabs.redistimeseries.Aggregation;
import com.redislabs.redistimeseries.Value;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import name.mrkandreev.tsplayer.dto.SeriesInput;
import name.mrkandreev.tsplayer.dto.SeriesMetaDto;
import name.mrkandreev.tsplayer.dto.ValueInput;
import name.mrkandreev.tsplayer.service.DataService;

@Path("/series")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeriesResource {
  @Inject
  DataService dataService;

  @POST
  public Boolean create(SeriesInput input) {
    return dataService.create(input);
  }

  @GET
  @Path("{key}")
  public SeriesMetaDto getMeta(@PathParam("key") String key) {
    return dataService.getMeta(key);
  }

  @GET
  @Path("{key}/values")
  public Value[] fetch(
      @PathParam("key") String key,
      @QueryParam("from") Long from,
      @QueryParam("to") Long to,
      @QueryParam("aggregation") Aggregation aggregation) {
    return dataService.fetch(key, from, to, aggregation);
  }

  @POST
  @Path("{key}/values")
  public void upload(@PathParam("key") String key, ValueInput... values) {
    dataService.upload(key, values);
  }
}
