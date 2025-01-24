package com.pawelnu.BackendProjectManager.dto.pearson;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeopleFilteringResponseDTO {

  private List<PersonDTO> projects;
  private PagingAndSortingMetadataDTO paging;
}
