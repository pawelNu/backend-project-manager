package com.pawelnu.BackendProjectManager.dto.ticketstatus;

import java.time.Instant;
import lombok.Data;

@Data
public class TicketStatusDTO {

  private Long seriesNumber;
  private String fromPerson;
  private String toPerson;
  private String fromStatus;
  private String toStatus;
  private Instant timestamp;
}
