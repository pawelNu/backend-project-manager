package com.pawelnu.projectmanager.endpoints.ticket.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class TicketDTO {
  private UUID id;
  private String number;
  private String title;
  private Instant deadline;
  private String additionalDetails;
  private UUID categoryId;
  private String categoryName;
  private UUID categoryValueId;
  private String categoryValue;
  private UUID priorityId;
  private String priorityName;
  private UUID priorityValueId;
  private String priorityValue;
  private UUID projectId;
  private String projectName;
  private UUID projectStepId;
  private String projectStepName;
}
