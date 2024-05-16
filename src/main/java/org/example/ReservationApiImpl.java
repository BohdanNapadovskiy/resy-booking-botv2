package org.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ReservationApiImpl implements ReservationApi {

    private ClientConfig config;

    public ReservationApiImpl(ClientConfig config) {
        this.config = config;
    }


    @Override
    public CompletableFuture<String> getReservations(ReservationDetails reservation) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lat", "0");
        queryParams.put("long", "0");
        queryParams.put("day", reservation.getDate());
        queryParams.put("party_size", String.valueOf(reservation.getPartySize()));
        queryParams.put("venue_id", String.valueOf(reservation.getVenuId()));
        return sendGetRequest("api.resy.com/4/find", queryParams);
    }

    @Override
    public CompletableFuture<String> getReservationDetails(ReservationDetails details) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("config_id", String.valueOf(details.getVenuId()));
        queryParams.put("day", details.getDate());
        queryParams.put("party_size", String.valueOf(details.getPartySize()));
        return sendGetRequest("api.resy.com/3/details", queryParams);
    }

    @Override
    public CompletableFuture<String> bookReservation(String bookToken, int paymentMethodId) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("book_token", bookToken);
        queryParams.put("struct_payment_method", String.format("{\"id\":%d}", paymentMethodId));
        return sendPostRequest("api.resy.com/3/book", queryParams);
    }


    private CompletableFuture<String> sendGetRequest(String baseUrl, Map<String, String> queryParams) {
        String url = "https://" + baseUrl + "?" + stringifyQueryParams(queryParams);
        return CompletableFuture.supplyAsync(() -> {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                request.setHeader("Authorization", String.format("ResyAPI api_key=\"%s\"", config.getApiKey()));
                request.setHeader("x-resy-auth-token", config.getAuth_token());
                try (CloseableHttpResponse response = client.execute(request)) {
                    return EntityUtils.toString(response.getEntity());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CompletableFuture<String> sendPostRequest(String baseUrl, Map<String, String> queryParams) {
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
                try (CloseableHttpResponse response = client.execute(request)) {
                    return EntityUtils.toString(response.getEntity());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String stringifyQueryParams(Map<String, String> queryParams) {
        return queryParams.entrySet().stream()
                .map(entry -> {
                    try {
                        return entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException("Encoding not supported", e);
                    }
                })
                .collect(Collectors.joining("&"));
    }
}
