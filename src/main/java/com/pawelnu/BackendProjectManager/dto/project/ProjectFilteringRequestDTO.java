package com.pawelnu.BackendProjectManager.dto.project;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingRequestDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProjectFilteringRequestDTO {

    private List<String> projectNameWords;

    private List<String> projectStatus;

    private PagingAndSortingRequestDTO pagingAndSortingRequestDTO = new PagingAndSortingRequestDTO();
}
