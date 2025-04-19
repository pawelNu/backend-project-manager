package com.pawelnu.projectmanager.dto.ticket;

import com.pawelnu.projectmanager.dto.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketsFilteringResponseDTO {

  private List<TicketDTO> tickets;
  private PagingAndSortingMetadataDTO paging;
}
