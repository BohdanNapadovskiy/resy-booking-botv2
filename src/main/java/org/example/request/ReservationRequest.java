package org.example.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.response.find.Slot;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

  private int commit;
  private String config_id;
  private String day;
  private int party_size;

  public ReservationRequest(Slot slot) {
    this.commit = 1;
    this.config_id = slot.getConfig().getToken();
  }


}
