package name.mrkandreev.tsplayer.service;

import com.redislabs.redistimeseries.Value;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import name.mrkandreev.avro.DataResponse;
import name.mrkandreev.avro.DataResponseValue;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

@ApplicationScoped
public class DataCompressionService {
  private static final boolean IS_SUCCESS = true;

  public ByteBuffer compress(String requestId, Value... values) {
    try {
      DataResponse dataResponse =
          new DataResponse(
              requestId,
              IS_SUCCESS,
              "",
              Stream.of(values)
                  .map(value -> new DataResponseValue(value.getTime(), value.getValue()))
                  .collect(Collectors.toList()));

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
      DatumWriter<DataResponse> writer = new SpecificDatumWriter<>(DataResponse.getClassSchema());
      writer.write(dataResponse, encoder);
      encoder.flush();
      return ByteBuffer.wrap(out.toByteArray());
    } catch (IOException e) {
      return ByteBuffer.wrap(new byte[0]);
    }
  }

  public ByteBuffer compressError(String requestId, String errorMessage) {
    try {
      DataResponse dataResponse =
          new DataResponse(
              requestId,
              !IS_SUCCESS,
              errorMessage,
              Collections.emptyList());

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
      DatumWriter<DataResponse> writer = new SpecificDatumWriter<>(DataResponse.getClassSchema());
      writer.write(dataResponse, encoder);
      encoder.flush();
      return ByteBuffer.wrap(out.toByteArray());
    } catch (IOException e) {
      return ByteBuffer.wrap(new byte[0]);
    }
  }

  public String getSchema() {
    return DataResponse.getClassSchema().toString();
  }
}
