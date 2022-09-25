package name.mrkandreev.tsplayer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionProvider implements ExceptionMapper<NotFoundException> {
  public NotFoundExceptionProvider() {
    // empty
  }

  @Override
  public Response toResponse(NotFoundException e) {
    return Response.status(Response.Status.NOT_FOUND).build();
  }
}
