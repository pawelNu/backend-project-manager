package com.pawelnu.BackendProjectManager.service;

import com.pawelnu.BackendProjectManager.dto.ticket.TIcketFilteringRequestDTO;
import com.pawelnu.BackendProjectManager.dto.ticket.TicketCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.ticket.TicketDTO;
import com.pawelnu.BackendProjectManager.dto.ticket.TicketsFilteringResponseDTO;
import java.util.UUID;

public interface TicketService {

  TicketsFilteringResponseDTO getAllTickets(
      Integer pageNumber, Integer pageSize, String field, Boolean isAscendingSorting);

  TicketDTO getTicketById(UUID id);

  TicketDTO getTicketByTitle(String title);

  TicketDTO createTicket(TicketCreateRequestDTO ticketCreateRequestDTO);

  String deleteTicketById(UUID id);

  TicketsFilteringResponseDTO searchProject(TIcketFilteringRequestDTO tIcketFilteringRequestDTO);

  //  TODO CRUD entry to ticket history
}
