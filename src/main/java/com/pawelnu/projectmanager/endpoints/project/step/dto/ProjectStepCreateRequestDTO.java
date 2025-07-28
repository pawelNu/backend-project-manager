package com.pawelnu.projectmanager.endpoints.project.step.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectStepCreateRequestDTO {

  @NotNull private String name;
  @NotNull private UUID projectId;
  @NotNull private UUID priorityValueId;
  @NotNull private UUID assignedEmployeeId;
  @NotNull private Instant deadline;
}
