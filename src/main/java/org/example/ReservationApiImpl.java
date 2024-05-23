package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class ReservationApiImpl implements ReservationApi {

  private ClientConfig config;
  private final Logger logger = LoggerFactory.getLogger(ReservationApiImpl.class);

  public ReservationApiImpl(ClientConfig config) {
    this.config = config;
  }


  @Override
  @SneakyThrows
  public FindResult findReservation() {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet httpGet = new HttpGet("https://api.resy.com/4/find");
      httpGet.setURI(createGetURI(httpGet));
      httpGet.setHeader("Host", "api.resy.com");
      httpGet.setHeader("Accept", "application/json");
      httpGet.setHeader("Referer", "https://resy.com/");
      httpGet.setHeader("Authorization", "ResyAPI api_key=\"VbWk7s3L4KiK5fzlO7JD3Q5EYolJI7n5\"");
      httpGet.setHeader(
          "User-Agent",
          "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36"
      );
      CloseableHttpResponse response = httpClient.execute(httpGet);
      if (response.getStatusLine().getStatusCode() == 200) {
        String jsonString = EntityUtils.toString(response.getEntity());
        logger.debug("Getting result from RESY API: {}", jsonString);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, FindResult.class);
      }
      else {
        logger.error("Cannot find any available reservations !!!");
        return new FindResult();
      }
    }
  }

  @Override
  @SneakyThrows
  public ReservationResponse getDetailedReservation(ReservationRequest request) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost("https://api.resy.com/3/details");
      URI uri = new URIBuilder(httpPost.getURI()).build();
      httpPost.setURI(uri);
      httpPost.setHeader("Accept", "*/*");
      httpPost.setHeader("Content-Type", "application/json");
      httpPost.setHeader("Authorization", "ResyAPI api_key=\"VbWk7s3L4KiK5fzlO7JD3Q5EYolJI7n5\"");
      httpPost.setEntity(createEntity(request));
      CloseableHttpResponse response = httpClient.execute(httpPost);
      String jsonString = EntityUtils.toString(response.getEntity());
      if (response.getStatusLine().getStatusCode() == 200) {
        logger.debug("Getting detailed response from RESY API by request {}", jsonString);
        return new ObjectMapper().readValue(jsonString, ReservationResponse.class);
      }
      else {
        logger.error("Error for getting a detailed response {}", jsonString);
        return new ReservationResponse();
      }
    }
  }

  @Override
  @SneakyThrows
  public void bookReservation(ReservationResponse reservation) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost("https://api.resy.com/3/book");
      URI uri = new URIBuilder(httpPost.getURI()).build();
      httpPost.setURI(uri);
      httpPost.setHeader("Host", "api.resy.com");
      httpPost.setHeader("Authorization", "ResyAPI api_key=\"VbWk7s3L4KiK5fzlO7JD3Q5EYolJI7n5\"");
      httpPost.setHeader("Cache-Control", "no-cache");
      ObjectMapper objectMapper = new ObjectMapper();
      String json = objectMapper.writeValueAsString(reservation);
      httpPost.setEntity(new StringEntity(json));
      CloseableHttpResponse response = httpClient.execute(httpPost);
      String jsonString = EntityUtils.toString(response.getEntity());
      if (response.getStatusLine().getStatusCode() == 200) {
        logger.debug("The reservation {} successfully booked", reservation.getBook_token());
      }
      else {
        logger.error("Error while booking the reservation {}", jsonString);
      }
    }
  }

  private HttpEntity createEntity(ReservationRequest request) {
    ObjectMapper objectMapper = new ObjectMapper();
    String json;
    try {
      json = objectMapper.writeValueAsString(request);
      return new StringEntity(json);
    }
    catch (JsonProcessingException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  private URI createGetURI(HttpGet httpGet) {
    try {
      return new URIBuilder(httpGet.getURI())
          .addParameter("lat", "0")
          .addParameter("long", "0")
          .addParameter("day", "2024-05-29")
          .addParameter("party_size", "6")
          .addParameter("venue_id", "60834")
          .build();
    }
    catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }


}
