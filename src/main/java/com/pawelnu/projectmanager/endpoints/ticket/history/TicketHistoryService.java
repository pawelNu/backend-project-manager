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
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketListResponseDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketRowDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
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
    return ticketHistoryMapper.toDTO(getTicketEntityById(id));
  }

  public TicketHistoryEntity getTicketEntityById(UUID id) {
    return ticketHistoryRepository
        .findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new NotFoundException(MSG.TICKET_HISTORY_NOT_FOUND + id));
  }

  public SimpleResponse deleteById(UUID id) {
    //    TicketHistoryEntity projectToDelete = getTicketEntityById(id);
    //    projectToDelete.setIsDeleted(true);
    //    TicketHistoryEntity projectDeleted = ticketHistoryRepository.save(projectToDelete);
    //    String item = "ticket history";
    //    if (projectDeleted.getIsDeleted()) {
    //      return SimpleResponse.builder().message(Shared.deleteMessage(item, id)).build();
    //    } else {
    //      return SimpleResponse.builder().message(Shared.cannotDeleteMessage(item, id)).build();
    //    }
    throw new NotImplementedException("not implemented");
  }

  public TicketHistoryDTO editById(UUID id, TicketHistoryEditRequestDTO body) {
    //    TicketEntity ticketToEdit = getTicketEntityById(id);
    //    CategoryValueEntity ticketCategory =
    //        categoryValueService.getCategoryValueById(body.getCategoryValueId());
    //    CategoryValueEntity ticketPriority =
    //        categoryValueService.getCategoryValueById(body.getPriorityValueId());
    //    ProjectEntity projectEntity = projectService.getProjectEntityById(body.getProjectId());
    //    ProjectStepEntity stepEntity =
    //        projectStepService.getProjectStepEntityById(body.getProjectStepId());
    //    ticketToEdit.setTitle(body.getTitle());
    //    ticketToEdit.setCategory(ticketCategory);
    //    ticketToEdit.setDeadline(body.getDeadline());
    //    ticketToEdit.setPriority(ticketPriority);
    //    ticketToEdit.setAdditionalDetails(body.getAdditionalDetails());
    //    ticketToEdit.setProject(projectEntity);
    //    ticketToEdit.setStep(stepEntity);
    //    TicketEntity updatedTicket = ticketHistoryRepository.save(ticketToEdit);
    //    return ticketHistoryMapper.toDTO(updatedTicket);
    throw new NotImplementedException("not implemented");
  }

  public TicketListResponseDTO filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    List<TicketRowDTO> results = ticketHistoryQueryRepository.getList(params);
    List<TicketHistoryDTO> projectDTOs = results.stream().map(ticketHistoryMapper::toDTO).toList();
    String contentRange =
        Shared.prepareContentRange(
            results.isEmpty() ? 0 : results.getFirst().getTotalElements(),
            params.getOffset(),
            params.getLimit());
    return TicketListResponseDTO.builder().data(projectDTOs).contentRange(contentRange).build();
  }
}
