package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.example.request.ReservationRequest;
import org.example.response.detail.ReservationResponse;
import org.example.response.find.FindResult;
import org.example.response.find.Venue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.example.response.find.Results;

import java.util.Optional;


public class ReservationClientImpl {

  private ReservationApi api;
  private final ExecutorService executorService = Executors.newFixedThreadPool(2);
  private static final Logger logger = Logger.getLogger(ReservationClientImpl.class.getName());

  public ReservationClientImpl(ClientConfig config) {
    this.api = new ReservationApiImpl(config);
  }

  @SneakyThrows
  public void bookReservation() {
    Results result = api.findReservation().getResults();
    result.getVenues().forEach(venue -> {
      try {
        getDetailedResponse(venue);
//        bookReservation();
      }
      catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void getDetailedResponse(Venue venue) throws InterruptedException, ExecutionException {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    venue.getSlots().stream()
        .map(slot -> (Callable<ReservationResponse>) () -> {
          TimeUnit.SECONDS.sleep(10);
          ReservationRequest request = new ReservationRequest(slot, 6);
          return api.getDetailedReservation(request);
        })
        .forEach(this::bookSpecificReservation);
    executor.shutdown();

  }

  private void bookSpecificReservation(Callable<ReservationResponse> response) {
    try {
      TimeUnit.SECONDS.sleep(10);
      ReservationResponse res = response.call();
      api.bookReservation(res);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
