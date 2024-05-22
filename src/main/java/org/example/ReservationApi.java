package org.example;

import org.example.request.ReservationRequest;
import org.example.response.detail.ReservationResponse;
import org.example.response.find.FindResult;

public interface ReservationApi {

    FindResult findReservation();

    ReservationResponse getDetailedReservation(ReservationRequest request);

    void bookReservation(ReservationResponse reservation);


}
