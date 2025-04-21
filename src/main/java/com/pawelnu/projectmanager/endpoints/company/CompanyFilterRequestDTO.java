package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.dto.PagingAndSortingRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyFilterRequestDTO {


  private String name;
  private String nip;
  private String regon;
  @Valid
  @NotNull(message = "Paging information must not be null.")
  private PagingAndSortingRequestDTO page = new PagingAndSortingRequestDTO();
}
