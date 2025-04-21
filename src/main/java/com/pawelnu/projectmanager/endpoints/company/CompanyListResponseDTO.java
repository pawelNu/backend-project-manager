package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.dto.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyListResponseDTO {

  private List<CompanyDTO> data;
  private PagingAndSortingMetadataDTO page;
}
