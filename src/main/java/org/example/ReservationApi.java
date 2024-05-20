package org.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.example.response.Results;
import org.example.response.Slot;
import org.example.response.Venue;
import org.example.response.detail.DetailedResponse;
import java.util.List;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ReservationApi {

    CloseableHttpResponse  getReservations(ReservationDetails details, String partySize) throws ExecutionException, InterruptedException, TimeoutException;

    CloseableHttpResponse getReservationDetails(List<Slot> slot) throws ExecutionException, InterruptedException, TimeoutException;

    CloseableHttpResponse bookReservation(DetailedResponse detailedResponse) throws ExecutionException, InterruptedException, TimeoutException;


}
