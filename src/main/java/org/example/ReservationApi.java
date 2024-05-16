package org.example;

import java.util.concurrent.CompletableFuture;

public interface ReservationApi {

    CompletableFuture<String> getReservations(ReservationDetails details);

    CompletableFuture<String> getReservationDetails(ReservationDetails details);

    CompletableFuture<String> bookReservation(String bookToken, int paymentMethodId);


}
