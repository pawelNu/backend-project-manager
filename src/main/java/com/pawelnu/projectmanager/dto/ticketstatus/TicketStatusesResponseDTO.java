package com.pawelnu.projectmanager.dto.ticketstatus;

import com.pawelnu.projectmanager.dto.PagingAndSortingMetadataDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketStatusesResponseDTO {

  private List<TicketStatusDTO> ticketStatuses;
  private PagingAndSortingMetadataDTO paging;
}
