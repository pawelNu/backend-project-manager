package com.pawelnu.projectmanager.service;

import com.pawelnu.projectmanager.dto.ticket.TIcketFilteringRequestDTO;
import com.pawelnu.projectmanager.dto.ticket.TicketCreateRequestDTO;
import com.pawelnu.projectmanager.dto.ticket.TicketDTO;
import com.pawelnu.projectmanager.dto.ticket.TicketsFilteringResponseDTO;
import java.util.UUID;

public interface TicketService {

  TicketsFilteringResponseDTO getAllTickets(
      Integer pageNumber, Integer pageSize, String field, Boolean isAscendingSorting);

  TicketDTO getTicketById(UUID id);

  TicketDTO getTicketByTitle(String title);

  TicketDTO createTicket(TicketCreateRequestDTO ticketCreateRequestDTO);

  String deleteTicketById(UUID id);

  TicketsFilteringResponseDTO searchProject(TIcketFilteringRequestDTO tIcketFilteringRequestDTO);

  //  TODO add missing services
  //  TODO for complex queries use QueryDSL
  //  TODO add controllers
}
