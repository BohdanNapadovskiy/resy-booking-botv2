package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetails {

    private String time;
    private int partySize;
    private String date;
    private String tableType;
    private String venuId;


}
