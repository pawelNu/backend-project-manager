package com.pawelnu.projectmanager.endpoints.ticket.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class TicketDTO {
  private UUID id;
  private String ticketNumber;
  private String title;
  private String additionalDetails;
  private UUID categoryId;
  private String categoryName;
  private UUID categoryValueId;
  private String categoryValue;
  private UUID companyId;
  private String companyName;
  private UUID priorityId;
  private String priorityName;
  private UUID priorityValueId;
  private String priorityValue;
  private UUID projectId;
  private String projectName;
  private UUID stepId;
  private String stepName;
  private Instant deadline;
}
