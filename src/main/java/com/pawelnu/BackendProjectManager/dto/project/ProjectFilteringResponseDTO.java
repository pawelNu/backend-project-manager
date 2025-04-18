package com.pawelnu.BackendProjectManager.dto.project;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectFilteringResponseDTO {

  private List<ProjectDTO> projects;
  private PagingAndSortingMetadataDTO paging;
}
