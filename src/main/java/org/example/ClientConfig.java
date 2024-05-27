package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientConfig {

  private String dateOfReservation;
  private String apiKey;
  private String auth_token;
  private String party_size;
  private String venue_id;
  private Integer snipeTimeHours;
  private Integer snipeTimeMinutes;
  private String[] timesTypes;
  private Integer repeatSnipeTime;
  private Integer beforeSnipeTime;


  public ClientConfig createConfig() {
    Properties properties = new Properties();
    ClientConfig config = new ClientConfig();
    ClassLoader loader = ClientConfig.class.getClassLoader();
    try (InputStream input = loader.getResourceAsStream("application.properties")) {
      properties.load(input);
      config.setApiKey(properties.getProperty("resyKeys.api-key"));
      config.setAuth_token(properties.getProperty("resyKeys.auth-token"));
      config.setSnipeTimeHours(Integer.valueOf(properties.getProperty("snipeTime.hours")));
      config.setSnipeTimeMinutes(Integer.valueOf(properties.getProperty("snipeTime.minutes")));
      config.setDateOfReservation(properties.getProperty("resDetails.date"));
      config.setVenue_id(properties.getProperty("resDetails.venue-id"));
      config.setParty_size(properties.getProperty("resDetails.party-size"));
      config.setTimesTypes(properties.getProperty("resDetails.res-time-types").split(","));
      config.setRepeatSnipeTime(Integer.valueOf(properties.getProperty("repeatSnipeTime.seconds")));
      config.setBeforeSnipeTime(Integer.valueOf(properties.getProperty("beforeSnipeTime.seconds")));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return config;

  }
}
