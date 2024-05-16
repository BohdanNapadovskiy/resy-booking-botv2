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

    private String time = "18:30:00";
    private int partySize = 4;
    private String date = "2024-05-17";
    private String tableType;
    private int venuId = 65452;


}
