package org.example;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ResyApi {

   private ExecutorService executor = Executors.newFixedThreadPool(2);
   private static final Logger logger = Logger.getLogger(BookingBot.class.getName());
   private ReservationApi client;

//    public void createReservation(List<ReservationDetails> reservationDetailsList) {
//        List<CompletableFuture<String>> futures = reservationDetailsList.stream()
//                .map(number -> CompletableFuture.supplyAsync(() -> processNumber(number))
//                        .thenAcceptAsync(result -> System.out.println("Processed number: " + number + ", Square: " + result)))
//                .collect(Collectors.toList());
//    }

    private Optional<String> snipeReservation(long millisToRetry, long dateTimeStart) {
        logger.info("Taking the shot...");
        logger.info("(҂‾ ▵‾)︻デ═一 (˚▽˚’!)/");
        logger.info("Attempting to snipe reservation for multiple party sizes");

//        Optional<String> result = Optional.empty();
//        if (!result.isPresent()) {
//            Optional<String> maybeConfigId = client.findReservations(
//                        resDetails.getDate(),
//                        partySize,
//                        resDetails.getPartySizes(),
//                        resDetails.getVenueId(),
//                        resDetails.getResTimeTypes()
//                );
//          }

//        return result;
      return null;
    }


}
