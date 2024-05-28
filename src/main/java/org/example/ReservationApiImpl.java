package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
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
    OkHttpClient client = new OkHttpClient();
    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.resy.com/4/find").newBuilder();
    urlBuilder.addQueryParameter("lat", "0");
    urlBuilder.addQueryParameter("long", "0");
    urlBuilder.addQueryParameter("day", config.getDateOfReservation());
    urlBuilder.addQueryParameter("party_size", config.getParty_size());
    urlBuilder.addQueryParameter("venue_id", config.getVenue_id());
    Request getRequest = new Request.Builder()
        .url(urlBuilder.build().toString())
        .header("Host", "api.resy.com")
        .header("Accept", "application/json")
        .header("Referer", "https://resy.com/")
        .header("Authorization", "ResyAPI api_key=\"VbWk7s3L4KiK5fzlO7JD3Q5EYolJI7n5\"")
        .header(
            "User-Agent",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36"
        )
        .build();
    Call call = client.newCall(getRequest);
    Response response = call.execute();
    String responseBody = response.body().string();
    if (response.code() == 200) {
      logger.info("Getting result from RESY API: {}", responseBody);
      return new ObjectMapper().readValue(responseBody, FindResult.class);
    }
    else {
      logger.error("Cannot find any available reservations !!!");
      return new FindResult();
    }
  }

  @Override
  @SneakyThrows
  public ReservationResponse getDetailedReservation(ReservationRequest request) {
    String json = createEntity(request);
    RequestBody body = RequestBody.create(
        MediaType.parse("application/json"), json);
    OkHttpClient client = new OkHttpClient();
    Request postRequest = new Request.Builder()
        .url("https://api.resy.com/3/details")
        .header("Authorization", "ResyAPI api_key=\"VbWk7s3L4KiK5fzlO7JD3Q5EYolJI7n5\"")
        .header("Accept-Encoding", "gzip, deflate, br, zstd")
        .header("Content-Type", "application/json")
        .header("Accept-Language", "en-US,en;q=0.9,ru;q=0.8,uk;q=0.7")
        .post(body)
        .build();
    Call call = client.newCall(postRequest);
    Response response = call.execute();
    String responseBody = response.body().string();
    if (response.code() == 201) {
      logger.info("Getting detailed response from RESY API by request {}", responseBody);
      return new ObjectMapper().readValue(responseBody, ReservationResponse.class);
    }
    else {
      logger.error("Error for getting a detailed response {}", responseBody);
      return new ReservationResponse();
    }
  }

  @Override
  @SneakyThrows
  public void bookReservation(ReservationResponse reservation) {
    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("book_token", String.valueOf(reservation.getBook_token().getValue()))
        .addFormDataPart("password", "resy.com-venue-details")
        .build();
    Request postRequest = new Request.Builder()
        .url("https://api.resy.com/3/book")
        .header("Authorization", "ResyAPI api_key=\"VbWk7s3L4KiK5fzlO7JD3Q5EYolJI7n5\"")
        .header("Accept-Encoding", "gzip, deflate, br, zstd")
        .header("Content-Type", "application/json")
        .header("Accept-Language", "en-US,en;q=0.9,ru;q=0.8,uk;q=0.7")
        .header("X-Resy-Universal-Auth", config.getApiKey())
        .post(requestBody)
        .build();
    Call call = client.newCall(postRequest);
    Response response = call.execute();
    String responseBody = response.body().string();
    if (response.code() == 201) {
      logger.info("The reservation {} successfully booked", reservation.getBook_token());
    }
    else {
      logger.error("Error while booking the reservation {}", responseBody);
    }
  }

  private String createEntity(ReservationRequest request) {
    ObjectMapper objectMapper = new ObjectMapper();
    String json;
    try {
      return objectMapper.writeValueAsString(request);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
