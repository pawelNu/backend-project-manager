package com.pawelnu.BackendProjectManager.dto.project;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingMetadataDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectFilteringResponseDTO {

    private List<ProjectDTO> projects;
    private PagingAndSortingMetadataDTO paging;
}
