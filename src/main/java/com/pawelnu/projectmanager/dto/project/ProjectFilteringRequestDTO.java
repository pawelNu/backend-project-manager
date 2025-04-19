package com.pawelnu.projectmanager.dto.project;

import com.pawelnu.projectmanager.dto.PagingAndSortingRequestDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ProjectFilteringRequestDTO {

  private List<String> projectNameKeywords = new ArrayList<>();
  private List<String> projectStatuses = new ArrayList<>();
  private PagingAndSortingRequestDTO paging = new PagingAndSortingRequestDTO();
}
