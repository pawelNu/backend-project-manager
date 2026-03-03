package com.pawelnu.projectmanager.endpoints.ticket.history.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class TicketHistoryDTO {
  private UUID id;
  private UUID ticketId;
  private String ticketNumber;
  private String ticketTitle;
  private UUID fromStatusId;
  private String fromStatusName;
  private UUID toStatusId;
  private String toStatusName;
  private UUID fromEmployeeId;
  private String fromEmployeeName;
  private UUID toEmployeeId;
  private String toEmployeeName;
  private String comment;
  private Instant created;
}
