package org.example.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailedResponse {

  private User user;
  private BookToken bookToken;

  public DetailedResponse() {
  }

  public DetailedResponse(User user, BookToken bookToken) {
    this.user = user;
    this.bookToken = bookToken;
  }


}
