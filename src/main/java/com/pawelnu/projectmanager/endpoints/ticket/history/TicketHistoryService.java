package com.pawelnu.projectmanager.endpoints.ticket.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.project.ProjectService;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepService;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketListResponseDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketRowDTO;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
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
  private final CategoryValueService categoryValueService;
  private final ProjectService projectService;
  private final ProjectStepService projectStepService;
  private final TicketHistoryMapper ticketHistoryMapper;
  private final ObjectMapper objectMapper;

  public TicketDTO create(TicketCreateRequestDTO body) {
    //    CategoryValueEntity ticketCategory =
    //        categoryValueService.getCategoryValueById(body.getCategoryValueId());
    //    CategoryValueEntity ticketPriority =
    //        categoryValueService.getCategoryValueById(body.getPriorityValueId());
    //    ProjectEntity projectEntity = projectService.getProjectEntityById(body.getProjectId());
    //    ProjectStepEntity stepEntity =
    //        projectStepService.getProjectStepEntityById(body.getProjectStepId());
    //    TicketEntity ticketEntity =
    //        TicketEntity.builder()
    //            .number(generateTicketNumber())
    //            .title(body.getTitle())
    //            .category(ticketCategory)
    //            .deadline(body.getDeadline())
    //            .priority(ticketPriority)
    //            .additionalDetails(body.getAdditionalDetails())
    //            .project(projectEntity)
    //            .step(stepEntity)
    //            .build();
    //    TicketEntity savedTicket = ticketHistoryRepository.save(ticketEntity);
    //    return ticketHistoryMapper.toDTO(savedTicket);
    throw new NotImplementedException("not implemented");
  }

  public TicketDTO getById(UUID id) {
    //    TicketEntity companyEntity = getTicketEntityById(id);
    //    return ticketHistoryMapper.toDTO(companyEntity);
    throw new NotImplementedException("not implemented");
  }

  public TicketHistoryEntity getTicketEntityById(UUID id) {
    //    return ticketHistoryRepository
    //        .findByIdAndIsDeletedFalse(id)
    //        .orElseThrow(() -> new NotFoundException(MSG.TICKET_NOT_FOUND + id));
    throw new NotImplementedException("not implemented");
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

  public TicketDTO editById(UUID id, TicketEditRequestDTO body) {
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
    List<TicketDTO> projectDTOs = results.stream().map(ticketHistoryMapper::toDTO).toList();
    String contentRange =
        Shared.prepareContentRange(
            results.isEmpty() ? 0 : results.getFirst().getTotalElements(),
            params.getOffset(),
            params.getLimit());
    return TicketListResponseDTO.builder().data(projectDTOs).contentRange(contentRange).build();
  }
}
