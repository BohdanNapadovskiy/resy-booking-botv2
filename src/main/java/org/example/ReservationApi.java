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

    CloseableHttpResponse  getReservations(ReservationDetails details, String partySize);

    List<DetailedResponse> getReservationDetails(List<Slot> slot);

    CloseableHttpResponse bookReservation(DetailedResponse detailedResponse);


}
