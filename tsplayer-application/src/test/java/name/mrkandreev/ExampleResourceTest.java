package name.mrkandreev;

import static io.restassured.RestAssured.given;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/** This is chained tests. Each one depends on previous. */
@QuarkusTest
@QuarkusTestResource(RedisResource.class)
class ExampleResourceTest {
  private static final String NAME = "myname";

  @Test
  void testCreateSeries() {
    Assert.assertEquals(
        200,
        given()
            .when()
            .body(Map.of("name", NAME))
            .contentType(ContentType.JSON)
            .post("/series")
            .thenReturn()
            .andReturn()
            .getStatusCode());
  }

  @Test
  void testUpload() {
    Assert.assertEquals(
        204,
        given()
            .when()
            .body(
                List.of(
                    Map.of("timestamp", 1600759277, "value", 42),
                    Map.of("timestamp", 1600759377, "value", 43),
                    Map.of("timestamp", 1600759477, "value", 44),
                    Map.of("timestamp", 1600759577, "value", 45)))
            .contentType(ContentType.JSON)
            .post(String.format("/series/%s/values", NAME))
            .thenReturn()
            .andReturn()
            .getStatusCode());
  }

  @Test
  void testFetch() {
    Assert.assertEquals(
        200,
        given()
            .when()
            .params(
                Map.of(
                    "from", "1600759277",
                    "to", "1600759577",
                    "aggregation", "AVG",
                    "timeBucket", "500"))
            .get(String.format("/series/%s/values", NAME))
            .thenReturn()
            .andReturn()
            .getStatusCode());
  }
}
