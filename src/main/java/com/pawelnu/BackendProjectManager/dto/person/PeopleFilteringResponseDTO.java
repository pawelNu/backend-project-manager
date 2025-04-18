package com.pawelnu.BackendProjectManager.dto.person;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeopleFilteringResponseDTO {

  private List<PersonDTO> people;
  private PagingAndSortingMetadataDTO paging;
}
