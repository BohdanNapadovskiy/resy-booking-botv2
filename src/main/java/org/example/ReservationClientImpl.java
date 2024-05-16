package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Optional;
import java.util.stream.Collectors;


public class ReservationClientImpl {

  private static final Logger logger = Logger.getLogger(ReservationClientImpl.class.getName());
  private ReservationApi api;
  private final ExecutorService executorService = Executors.newFixedThreadPool(2);

  public ReservationClientImpl(ReservationApi api) {
    this.api = api;

  }

  public void findReservations(List<ReservationDetails> reservation) {
    reservation.stream().forEach(details-> {
      String response = null;
      try {
        response = api.getReservationDetails(details).get(10, TimeUnit.SECONDS);
        logger.info("URL Response: " + response);
        Map<String, Map<String, String>> reservationMap = parseReservationMap(response);
        int a = 1;
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      catch (ExecutionException e) {
        throw new RuntimeException(e);
      }
      catch (TimeoutException e) {
        throw new RuntimeException(e);
      }

    });
//        .map(details -> CompletableFuture.supplyAsync(
//            () -> {
//              String response = "";
//              try {
//                response = api.getReservationDetails(details).get(2, TimeUnit.SECONDS);
//                logger.info("URL Response: " + response);
//                Map<String, Map<String, String>> reservationMap = parseReservationMap(response);
//                if (!reservationMap.isEmpty()) {
//                  int a = 1;
////              findReservationTime(reservationMap);
//                }
//                else {
//                  logger.warning("No available reservations");
//                  return Optional.empty();
//                }
//              }
//              catch (InterruptedException | ExecutionException | TimeoutException e) {
//                throw new RuntimeException(e);
//              }
//              return response;
//            },
//            executorService
//        ));
  }

//  public Optional<BookingDetails> getReservationDetails(String configId, String date, int partySize) {
//    try {
//      String response = api.getReservationDetails(configId, date, partySize).get(5, TimeUnit.SECONDS);
//      logger.info("URL Response: " + response);
//
//      JsonElement resDetails = JsonParser.parseString(response);
//      String paymentMethodId = resDetails.getAsJsonObject().get("user").getAsJsonObject().get("payment_methods")
//          .getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
//      String bookToken = resDetails.getAsJsonObject().get("book_token").getAsJsonObject().get("value").getAsString();
//
//      logger.info("Payment Method Id: " + paymentMethodId);
//      logger.info("Book Token: " + bookToken);
//
//      return Optional.of(new BookingDetails(Integer.parseInt(paymentMethodId), bookToken));
//    }
//    catch (InterruptedException | ExecutionException | TimeoutException e) {
//      logger.severe("Error retrieving reservation details: " + e.getMessage());
//      return Optional.empty();
//    }
//  }

//  public Optional<String> bookReservation(int paymentMethodId, String bookToken) {
//    try {
//      String response = api.postReservation(paymentMethodId, bookToken).get(10, TimeUnit.SECONDS);
//      logger.info("URL Response: " + response);
//
//      String resyToken = JsonParser.parseString(response).getAsJsonObject().get("resy_token").getAsString();
//
//      logger.info("Successfully sniped reservation");
//      logger.info("Resy token is " + resyToken);
//
//      return Optional.of(resyToken);
//    }
//    catch (InterruptedException | ExecutionException | TimeoutException e) {
//      logger.severe("Error booking reservation: " + e.getMessage());
//      return Optional.empty();
//    }
//  }

//  private Optional<String> retryFindReservations(ReservationDetails reservation) {
//        try {
//            String response = api.getReservations(date, partySize, venueId).get(5, TimeUnit.SECONDS);
//            Map<String, Map<String, String>> reservationMap = parseReservationMap(response);
//
//            if (!reservationMap.isEmpty()) {
//                return findReservationTime(reservationMap, resTimeTypes);
//            } else if (System.currentTimeMillis() - startTime < millisToRetry) {
//                return retryFindReservations(date, partySize, venueId, resTimeTypes, millisToRetry, startTime);
//            } else {
//                logger.warning("No available reservations");
//                return Optional.empty();
//            }
//        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            logger.severe("Error finding reservations: " + e.getMessage());
//            return Optional.empty();
//        }
//    }

  private Map<String, Map<String, String>> parseReservationMap(String jsonResponse) {
    Map<String, Map<String, String>> reservationMap = new HashMap<>();
    JsonElement slots = JsonParser.parseString(jsonResponse).getAsJsonObject().getAsJsonArray("results").get(0)
        .getAsJsonObject().getAsJsonArray("venues").get(0).getAsJsonObject().getAsJsonArray("slots");
    for (JsonElement slot : slots.getAsJsonArray()) {
      String time = slot.getAsJsonObject().get("date").getAsJsonObject().get("start").getAsString().substring(11, 16);
      String type = slot.getAsJsonObject().get("config").getAsJsonObject().get("type").getAsString().toLowerCase();
      String token = slot.getAsJsonObject().get("config").getAsJsonObject().get("token").getAsString();
      reservationMap.computeIfAbsent(time, k -> new HashMap<>()).put(type, token);
    }

    return reservationMap;
  }

//    private Optional<String> findReservationTime (){
//      for (Map.Entry<String, Map<String, String>> entry : resTimeTypes.entrySet()) {
//        Map<String, String> availableTypes = reservationMap.get(entry.getKey());
//        if (availableTypes != null) {
//          for (Map.Entry<String, String> typeEntry : entry.getValue().entrySet()) {
//            String configId = availableTypes.get(typeEntry.getKey());
//            if (configId != null) {
//              logger.info("Config Id: " + configId);
//              return Optional.of(configId);
//            }
//          }
//        }
//      }
//
//      logger.warning("Could not find a reservation for the given times");
//      return Optional.empty();
//    }

}
