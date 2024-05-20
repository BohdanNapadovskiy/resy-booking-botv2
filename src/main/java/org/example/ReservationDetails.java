package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetails {

    private String time = "18:30:00";
    private List<Integer> partySizes;
    private int venuId = 65452;
    private String date;
    List<String> dates = new ArrayList<>();
    List<Integer> tablesType = new ArrayList<>();


}
