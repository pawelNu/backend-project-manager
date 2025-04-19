package com.pawelnu.projectmanager.dto.person;

import com.pawelnu.projectmanager.dto.PagingAndSortingRequestDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PersonFilteringRequestDTO {

  private List<String> personFirstNames = new ArrayList<>();
  private List<String> personLastNames = new ArrayList<>();
  private List<String> personRoles = new ArrayList<>();
  private PagingAndSortingRequestDTO paging = new PagingAndSortingRequestDTO();
}
