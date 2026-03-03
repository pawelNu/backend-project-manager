package com.pawelnu.projectmanager.endpoints.ticket.history.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketHistoryEditRequestDTO {
  @NotNull private UUID ticketId;
  @NotNull private UUID fromStatusId;
  @NotNull private UUID toStatusId;
  @NotNull private UUID fromEmployeeId;
  @NotNull private UUID toEmployeeId;
  private String comment;
}
