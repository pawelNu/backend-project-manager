package com.pawelnu.projectmanager.dto.person;

import com.pawelnu.projectmanager.dto.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeopleFilteringResponseDTO {

  private List<PersonDTO> people;
  private PagingAndSortingMetadataDTO paging;
}
