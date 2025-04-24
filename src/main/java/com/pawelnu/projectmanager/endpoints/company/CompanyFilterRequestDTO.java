package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.dto.PagingAndSortingRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CompanyFilterRequestDTO {

  private List<String> names;
  private List<String> nips;
  private List<String> regons;

  @Valid
  @NotNull(message = "Paging information must not be null.")
  private PagingAndSortingRequestDTO page = new PagingAndSortingRequestDTO();
}
