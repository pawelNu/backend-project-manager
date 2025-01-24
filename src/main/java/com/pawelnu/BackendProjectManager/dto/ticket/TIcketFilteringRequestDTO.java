package com.pawelnu.BackendProjectManager.dto.ticket;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingRequestDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TIcketFilteringRequestDTO {

  private List<String> ticketTitles = new ArrayList<>();
  private PagingAndSortingRequestDTO paging = new PagingAndSortingRequestDTO();
}
