package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.request.ReservationRequest;
import org.example.response.detail.ReservationResponse;
import org.example.response.find.FindResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class ReservationApiImpl implements ReservationApi {

  private ClientConfig config;
  private final Logger logger = LoggerFactory.getLogger(ReservationApiImpl.class);

  public ReservationApiImpl(ClientConfig config) {
    this.config = config;
  }


  @Override
  public FindResult findReservation() {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet httpGet = new HttpGet("https://api.resy.com/4/find");
      URI uri = new URIBuilder(httpGet.getURI())
          .addParameter("lat", "0")
          .addParameter("long", "0")
          .addParameter("day", "2024-05-29")
          .addParameter("party_size", "2")
          .addParameter("venue_id", "60834")
          .build();
      httpGet.setURI(uri);
      httpGet.setHeader("Host", "api.resy.com");
      httpGet.setHeader("Accept", "application/json");
      httpGet.setHeader("Authorization", "ResyAPI api_key=\"VbWk7s3L4KiK5fzlO7JD3Q5EYolJI7n5\"");
      httpGet.setHeader(
          "User-Agent",
          "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36"
      );
      CloseableHttpResponse response = httpClient.execute(httpGet);
      String jsonString = EntityUtils.toString(response.getEntity());
      logger.info("Getting result from RESY API: {}", jsonString);
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(jsonString, FindResult.class);
    }
    catch (Exception e) {
      logger.info("Cannot find any available reservations !!!");
      return new FindResult();
    }
  }

  @Override
  public ReservationResponse getDetailedReservation(ReservationRequest request) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost("https://api.resy.com/3/details");
      URI uri = new URIBuilder(httpPost.getURI()).build();
      httpPost.setURI(uri);
      httpPost.setHeader("Host", "api.resy.com");
      httpPost.setHeader("Authorization", "ResyAPI api_key=\"VbWk7s3L4KiK5fzlO7JD3Q5EYolJI7n5\"");
      httpPost.setHeader("Cache-Control", "no-cache");

      ObjectMapper objectMapper = new ObjectMapper();
      String json = objectMapper.writeValueAsString(request);
      StringEntity entity = new StringEntity(json);
      httpPost.setEntity(entity);
      CloseableHttpResponse response = httpClient.execute(httpPost);
      String jsonString = EntityUtils.toString(response.getEntity());
      logger.info("Getting detailed response from RESY API by request {}", jsonString);
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(jsonString, ReservationResponse.class);
    }
    catch (Exception e) {
      logger.info("Cannot find any available reservations for config_Id {}", request.getConfig_id());
      return new ReservationResponse();
    }
  }

  @Override
  public void bookReservation(ReservationResponse d) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost("https://api.resy.com/3/book");
      URI uri = new URIBuilder(httpPost.getURI()).build();
      httpPost.setURI(uri);
      httpPost.setHeader("Host", "api.resy.com");
      httpPost.setHeader("Authorization", "ResyAPI api_key=\"VbWk7s3L4KiK5fzlO7JD3Q5EYolJI7n5\"");
      httpPost.setHeader("Cache-Control", "no-cache");

      ObjectMapper objectMapper = new ObjectMapper();
//      String json = objectMapper.writeValueAsString(request);
//      StringEntity entity = new StringEntity(json);
//      httpPost.setEntity(entity);
      CloseableHttpResponse response = httpClient.execute(httpPost);
      String jsonString = EntityUtils.toString(response.getEntity());
      logger.info("Getting detailed response from RESY API by request {}", jsonString);
      ObjectMapper mapper = new ObjectMapper();
    }
    catch (Exception e) {
//      logger.info("Cannot find any available reservations for config_Id {}", request.getConfig_id());
    }

  }

}
