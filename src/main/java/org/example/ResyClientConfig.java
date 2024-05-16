package org.example;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResyClientConfig {

    private String dateOfReservation;
    private String venue_id;
    private String apiKey;
    private String auth_token;
}
