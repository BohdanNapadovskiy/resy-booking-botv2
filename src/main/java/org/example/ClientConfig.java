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
    private Integer snipeTimeHours;
    private Integer snipeTimeMinutes;
    private String date;


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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;

    }
}
