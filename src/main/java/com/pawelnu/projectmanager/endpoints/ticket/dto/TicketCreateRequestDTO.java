package com.pawelnu.projectmanager.endpoints.ticket.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketCreateRequestDTO {

  @NotNull
  @Size(min = 5, message = "Title must be at least 5 characters")
  private String title;

  @NotNull private UUID ticketCategoryId;
  @NotNull private Instant deadline;
  @NotNull private UUID ticketPriorityId;
  private String additionalDetails;
  @NotNull private UUID projectId;
  @NotNull private UUID stepId;
}
