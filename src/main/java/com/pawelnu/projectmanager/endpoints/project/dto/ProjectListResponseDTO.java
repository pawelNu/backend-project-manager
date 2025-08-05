package com.pawelnu.projectmanager.endpoints.project.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectListResponseDTO {
  private List<ProjectDTO> data;
  private String contentRange;
}
