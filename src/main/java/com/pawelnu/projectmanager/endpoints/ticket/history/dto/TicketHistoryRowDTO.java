package com.pawelnu.projectmanager.endpoints.ticket.history.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketHistoryRowDTO {
  private UUID id;
  private UUID ticketId;
  private String ticketNumber;
  private String ticketTitle;
  private UUID fromStatusId;
  private String fromStatusName;
  private UUID toStatusId;
  private String toStatusName;
  private UUID fromEmployeeId;
  private String fromEmployeeFirstName;
  private String fromEmployeeLastName;
  private UUID toEmployeeId;
  private String toEmployeeFirstName;
  private String toEmployeeLastName;
  private String comment;
  private Instant created;
  private Long totalElements;
  private Integer totalPages;
}
