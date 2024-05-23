package org.example;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.example.request.ReservationRequest;
import org.example.response.detail.ReservationResponse;
import org.example.response.find.Slot;
import org.example.response.find.Venue;
import lombok.SneakyThrows;
import org.example.response.find.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReservationClientImpl {

  private ReservationApi api;
  private ClientConfig config;
  private final ExecutorService executorService = Executors.newFixedThreadPool(2);
  private final Logger logger = LoggerFactory.getLogger(ReservationClientImpl.class);

  public ReservationClientImpl(ClientConfig config) {
    this.config = config;
    this.api = new ReservationApiImpl(config);
  }

  @SneakyThrows
  public void bookReservations() {
    Results result = api.findReservation().getResults();
    if (!result.getVenues().isEmpty())
      result.getVenues().forEach(this::bookSpecificReservation);
    else
      logger.error("There are no available slots for date of reservation: {}", config.getDateOfReservation());
  }

  private void bookSpecificReservation(Venue venue) {
    venue.getSlots().stream()
        .filter(this::existDateInProperties)
        .forEach(slot -> {
          try {
            TimeUnit.SECONDS.sleep(10);
            ReservationRequest request = new ReservationRequest(slot);
            ReservationResponse response = api.getDetailedReservation(request);
            api.bookReservation(response);
          }
          catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private Boolean existDateInProperties(Slot slot) {
    String[] date = slot.getDate().getStart().split(" ");
    String time = date[1].trim();
    return Arrays.asList(config.getTimesTypes()).contains(time);
  }

}
