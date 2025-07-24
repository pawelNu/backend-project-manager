package com.pawelnu.projectmanager.endpoints.project.step.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectStepListResponseDTO {
  private List<ProjectStepDTO> data;
  private String contentRange;
}
