package com.pawelnu.projectmanager.endpoints.project.step.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectStepCreateRequestDTO {

  @NotNull
  @Size(min = 5, max = 255, message = "Project step name must be 5-255 characters")
  private String name;

  @NotNull private UUID projectId;
  @NotNull private UUID priorityValueId;
  @NotNull private UUID assignedEmployeeId;

  @Schema(
      description = "Time with format Instant (UTC), example: 2025-07-29T12:00:00Z",
      example = "2025-07-29T12:00:00Z")
  @NotNull
  private Instant deadline;
}
