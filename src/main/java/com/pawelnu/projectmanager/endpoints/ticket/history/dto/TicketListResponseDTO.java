package com.pawelnu.projectmanager.endpoints.ticket.history.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketListResponseDTO {
  private List<TicketDTO> data;
  private String contentRange;
}
