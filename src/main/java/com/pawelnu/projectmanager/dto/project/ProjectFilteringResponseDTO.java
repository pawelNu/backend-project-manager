package com.pawelnu.projectmanager.dto.project;

import com.pawelnu.projectmanager.dto.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectFilteringResponseDTO {

  private List<ProjectDTO> projects;
  private PagingAndSortingMetadataDTO paging;
}
