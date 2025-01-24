package com.pawelnu.BackendProjectManager.dto.ticket;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketsFilteringResponseDTO {

  private List<TicketDTO> tickets;
  private PagingAndSortingMetadataDTO paging;
}
