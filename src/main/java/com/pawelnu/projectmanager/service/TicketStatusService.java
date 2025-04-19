package com.pawelnu.projectmanager.service;

import com.pawelnu.projectmanager.dto.ticketstatus.TicketStatusDTO;
import com.pawelnu.projectmanager.dto.ticketstatus.TicketStatusesResponseDTO;
import java.time.Instant;
import java.util.UUID;

public interface TicketStatusService {
  TicketStatusesResponseDTO getAllTicketStatuses(Long ticketSeriesNumber);

  TicketStatusesResponseDTO getTicketsStatusesByDateRange(Instant fromDate, Instant toDate);

  TicketStatusDTO getTicketStatusById(UUID id);

  TicketStatusDTO changeTicketStatus(TicketStatusDTO ticketStatusDTO);

  TicketStatusDTO withdrawTicketLastStatus(Long ticketSeriesNumber);
}
