package name.mrkandreev.tsplayer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StorageConnectionErrorProvider implements ExceptionMapper<StorageConnectionError> {
  public StorageConnectionErrorProvider() {
    // empty
  }

  @Override
  public Response toResponse(StorageConnectionError e) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
  }
}
