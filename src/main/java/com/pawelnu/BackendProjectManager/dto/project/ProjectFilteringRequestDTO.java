package com.pawelnu.BackendProjectManager.dto.project;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingRequestDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ProjectFilteringRequestDTO {

    private List<String> projectNameKeywords = new ArrayList<>(); // TODO not tested
    private List<String> projectStatuses = new ArrayList<>(); // TODO not tested
    private PagingAndSortingRequestDTO pagingAndSortingRequestDTO =
            new PagingAndSortingRequestDTO();
}
