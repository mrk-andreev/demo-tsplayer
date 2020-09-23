package name.mrkandreev.tsplayer.resource;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import name.mrkandreev.tsplayer.dto.DataRequest;
import name.mrkandreev.tsplayer.service.DataCompressionService;
import name.mrkandreev.tsplayer.service.DataService;

@ServerEndpoint("/ws")
@ApplicationScoped
public class SeriesSocket {
  private static final JsonMapper mapper = new JsonMapper();

  @Inject DataService dataService;

  @Inject DataCompressionService dataCompressionService;

  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    DataRequest request = mapper.readValue(message, DataRequest.class);

    session
        .getAsyncRemote()
        .sendBinary(
            dataCompressionService.compress(
                dataService.fetch(
                    request.getKey(),
                    request.getFrom(),
                    request.getTo(),
                    request.getAggregation(),
                    request.getTimeBucket())));
  }
}
