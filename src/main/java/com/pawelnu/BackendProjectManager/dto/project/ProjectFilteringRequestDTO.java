package com.pawelnu.BackendProjectManager.dto.project;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingRequestDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectFilteringRequestDTO {

    private List<String> projectNameKeywords = new ArrayList<>();
    private List<String> projectStatuses = new ArrayList<>();
    private PagingAndSortingRequestDTO paging =
            new PagingAndSortingRequestDTO();
}
