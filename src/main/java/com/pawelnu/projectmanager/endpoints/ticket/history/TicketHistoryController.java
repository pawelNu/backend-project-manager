package com.pawelnu.projectmanager.endpoints.ticket.history;

import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketListResponseDTO;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicketHistoryController implements TicketHistoryApi {

  private final TicketHistoryService ticketHistoryService;

  @Override
  public ResponseEntity<TicketDTO> create(TicketCreateRequestDTO body) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ticketHistoryService.create(body));
  }

  @Override
  public ResponseEntity<List<TicketDTO>> getList(String sort, String range, String filter) {
    TicketListResponseDTO result = ticketHistoryService.filter(sort, range, filter);
    return ResponseEntity.ok()
        .header("Content-Range", result.getContentRange())
        .body(result.getData());
  }

  @Override
  public ResponseEntity<TicketDTO> getById(UUID id) {
    return ResponseEntity.ok(ticketHistoryService.getById(id));
  }

  @Override
  public ResponseEntity<TicketDTO> editById(UUID id, TicketEditRequestDTO body) {
    return ResponseEntity.ok(ticketHistoryService.editById(id, body));
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    return ResponseEntity.ok(ticketHistoryService.deleteById(id));
  }
}
