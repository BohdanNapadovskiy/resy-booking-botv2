package org.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.response.Slot;
import org.example.response.detail.DetailedResponse;
import org.example.response.Venue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class ReservationApiImpl implements ReservationApi {

  private ClientConfig config;

  public ReservationApiImpl(ClientConfig config) {
    this.config = config;
  }


  @Override
  public CloseableHttpResponse getReservations(ReservationDetails reservation, String partySize)
      throws ExecutionException, InterruptedException, TimeoutException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("lat", "0");
    queryParams.put("long", "0");
    queryParams.put("day", reservation.getDate());
    queryParams.put("party_size", partySize);
    queryParams.put("venue_id", String.valueOf(reservation.getVenuId()));
    return sendGetRequest("api.resy.com/4/find", queryParams).get(5, TimeUnit.SECONDS);
  }

  @Override
  public CloseableHttpResponse getReservationDetails(List<Slot> slots)
      throws ExecutionException, InterruptedException, TimeoutException {
    slots.stream().forEach(slot -> {
      Map<String, String> queryParams = new HashMap<>();
      queryParams.put("config_id", slot.getConfig().getToken());
      queryParams.put("day", slot.getDate());
      queryParams.put("party_size", slot.getConfig().getType());
      Thread.sleep(2000);
      return sendGetRequest("api.resy.com/3/details", queryParams).get(5, TimeUnit.SECONDS);
    });
  }

  @Override
  public CloseableHttpResponse bookReservation(DetailedResponse response)
      throws ExecutionException, InterruptedException, TimeoutException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("book_token", response.getBookToken().getValue());
    queryParams.put("struct_payment_method", String.format("{\"id\":%d}", paymentMethodId));
    return sendPostRequest("api.resy.com/3/book", queryParams).get(5, TimeUnit.SECONDS);
  }


  private CompletableFuture<CloseableHttpResponse> sendGetRequest(String baseUrl, Map<String, String> queryParams) {
    String url = "https://" + baseUrl + "?" + stringifyQueryParams(queryParams);
    return CompletableFuture.supplyAsync(() -> {
      try (CloseableHttpClient client = HttpClients.createDefault()) {
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", String.format("ResyAPI api_key=\"%s\"", config.getApiKey()));
        request.setHeader("x-resy-auth-token", config.getAuth_token());
        return client.execute(request);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private CompletableFuture<CloseableHttpResponse> sendPostRequest(String baseUrl, Map<String, String> queryParams) {
    String url = "https://" + baseUrl;
    String postParams = stringifyQueryParams(queryParams);
    return CompletableFuture.supplyAsync(() -> {
      try (CloseableHttpClient client = HttpClients.createDefault()) {
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeader("Origin", "https://widgets.resy.com");
        request.setHeader("Referer", "https://widgets.resy.com");
        request.setHeader("Authorization", String.format("ResyAPI api_key=\"%s\"", config.getApiKey()));
        request.setHeader("x-resy-auth-token", config.getAuth_token());
        request.setEntity(new org.apache.http.entity.StringEntity(postParams));
        return client.execute(request);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private String stringifyQueryParams(Map<String, String> queryParams) {
    return queryParams.entrySet().stream()
        .map(entry -> {
          try {
            return entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
          }
          catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported", e);
          }
        })
        .collect(Collectors.joining("&"));
  }
}
