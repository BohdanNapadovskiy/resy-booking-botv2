package org.example;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import org.example.response.detail.DetailedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.example.response.Results;

import java.util.Optional;


public class ReservationClientImpl {

  private ReservationApi api;
  private ClientConfig config;
  private final ExecutorService executorService = Executors.newFixedThreadPool(2);
  private static final Logger logger = Logger.getLogger(ReservationClientImpl.class.getName());

  public ReservationClientImpl(ClientConfig config) {
    this.config = config;
    this.api = new ReservationApiImpl(config);
  }

  @SneakyThrows
  public void findReservations(ReservationDetails details, List<Integer> partySizes) {
  details.getDates()
          .forEach(partySize-> {
            executorService.submit(()-> {
              CloseableHttpResponse response = api.getReservations(details, partySize);
              handleResponse(response)
                      .map(r-> {
                        r.getVenues().stream()
                                .forEach(v-> {
                                  DetailedResponse detailedResponse = api.getReservationDetails(v.getSlots())
                                  api.bookReservation(detailedResponse);
                                });
                      })
                      .orElseThrow()             ;
            });
          });
  }



  private Optional<Results> handleResponse(CloseableHttpResponse response) {
    Results results = null;
    if (response.getStatusLine().getStatusCode() == 200) {
      ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = EntityUtils.toString(response.getEntity());
           results=  objectMapper.readValue(jsonString, Results.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } else {
      logger.info("Missed the shot!");
      logger.info("┻━┻ ︵ /(°□°)/ ︵ ┻━┻'");
//      logger.info(noAvailableResMsg);
    }
    return Optional.of(results) ;
  }

}
