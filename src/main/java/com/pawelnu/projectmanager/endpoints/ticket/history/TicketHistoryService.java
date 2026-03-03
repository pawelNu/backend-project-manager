package com.pawelnu.projectmanager.endpoints.ticket.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import com.pawelnu.projectmanager.endpoints.project.ProjectService;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepService;
import com.pawelnu.projectmanager.endpoints.ticket.TicketEntity;
import com.pawelnu.projectmanager.endpoints.ticket.TicketService;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryRowDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketListResponseDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TicketHistoryService {

  private final TicketHistoryRepository ticketHistoryRepository;
  private final TicketHistoryQueryRepository ticketHistoryQueryRepository;
  private final TicketService ticketService;
  private final CategoryValueService categoryValueService;
  private final EmployeeService employeeService;
  private final ProjectService projectService;
  private final ProjectStepService projectStepService;
  private final TicketHistoryMapper ticketHistoryMapper;
  private final ObjectMapper objectMapper;

  public TicketHistoryDTO create(TicketHistoryCreateRequestDTO body) {
    TicketEntity ticket = ticketService.getTicketEntityById(body.getTicketId());
    CategoryValueEntity fromStatus =
        categoryValueService.getCategoryValueById(body.getFromStatusId());
    CategoryValueEntity toStatus = categoryValueService.getCategoryValueById(body.getToStatusId());
    EmployeeEntity fromEmployee = employeeService.getEmployeeEntityById(body.getFromEmployeeId());
    EmployeeEntity toEmployee = employeeService.getEmployeeEntityById(body.getToEmployeeId());
    TicketHistoryEntity ticketHistoryEntity =
        TicketHistoryEntity.builder()
            .ticket(ticket)
            .fromStatus(fromStatus)
            .toStatus(toStatus)
            .fromEmployee(fromEmployee)
            .toEmployee(toEmployee)
            .comment(body.getComment())
            .build();
    TicketHistoryEntity savedHistory = ticketHistoryRepository.save(ticketHistoryEntity);
    return ticketHistoryMapper.toDTO(savedHistory);
  }

  public TicketHistoryDTO getById(UUID id) {
    return ticketHistoryMapper.toDTO(getTicketHistoryEntityById(id));
  }

  public TicketHistoryEntity getTicketHistoryEntityById(UUID id) {
    return ticketHistoryRepository
        .findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new NotFoundException(MSG.TICKET_HISTORY_NOT_FOUND + id));
  }

  public SimpleResponse deleteById(UUID id) {
    TicketHistoryEntity historyToDelete = getTicketHistoryEntityById(id);
    historyToDelete.setIsDeleted(true);
    TicketHistoryEntity historyDeleted = ticketHistoryRepository.save(historyToDelete);
    String item = "ticket history";
    if (historyDeleted.getIsDeleted()) {
      return SimpleResponse.builder().message(Shared.deleteMessage(item, id)).build();
    } else {
      return SimpleResponse.builder().message(Shared.cannotDeleteMessage(item, id)).build();
    }
  }

  public TicketHistoryDTO editById(UUID id, TicketHistoryEditRequestDTO body) {
    TicketHistoryEntity historyToEdit = getTicketHistoryEntityById(id);
    TicketEntity ticket = ticketService.getTicketEntityById(body.getTicketId());
    CategoryValueEntity fromStatus =
        categoryValueService.getCategoryValueById(body.getFromStatusId());
    CategoryValueEntity toStatus = categoryValueService.getCategoryValueById(body.getToStatusId());
    EmployeeEntity fromEmployee = employeeService.getEmployeeEntityById(body.getFromEmployeeId());
    EmployeeEntity toEmployee = employeeService.getEmployeeEntityById(body.getToEmployeeId());
    historyToEdit.setTicket(ticket);
    historyToEdit.setFromStatus(fromStatus);
    historyToEdit.setToStatus(toStatus);
    historyToEdit.setFromEmployee(fromEmployee);
    historyToEdit.setToEmployee(toEmployee);
    historyToEdit.setComment(body.getComment());
    TicketHistoryEntity updatedTicket = ticketHistoryRepository.save(historyToEdit);
    return ticketHistoryMapper.toDTO(updatedTicket);
  }

  public TicketListResponseDTO getList(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    List<TicketHistoryRowDTO> results = ticketHistoryQueryRepository.getList(params);
    List<TicketHistoryDTO> projectDTOs = results.stream().map(ticketHistoryMapper::toDTO).toList();
    String contentRange =
        Shared.prepareContentRange(
            results.isEmpty() ? 0 : results.getFirst().getTotalElements(),
            params.getOffset(),
            params.getLimit());
    return TicketListResponseDTO.builder().data(projectDTOs).contentRange(contentRange).build();
  }
}
