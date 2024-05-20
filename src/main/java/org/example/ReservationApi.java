package org.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.example.response.Results;
import org.example.response.Slot;
import org.example.response.Venue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ReservationApi {

    CloseableHttpResponse  getReservations(ReservationDetails details, String partySize) throws ExecutionException, InterruptedException, TimeoutException;

    CloseableHttpResponse getReservationDetails(Slot slot) throws ExecutionException, InterruptedException, TimeoutException;

    CloseableHttpResponse bookReservation(String bookToken, int paymentMethodId);


}
