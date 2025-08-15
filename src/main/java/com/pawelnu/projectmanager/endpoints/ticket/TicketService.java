package com.pawelnu.projectmanager.endpoints.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.project.ProjectEntity;
import com.pawelnu.projectmanager.endpoints.project.ProjectService;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepEntity;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepService;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketListResponseDTO;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketRowDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.time.Year;
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
public class TicketService {

  private final TicketRepository ticketRepository;
  private final TicketQueryRepository ticketQueryRepository;
  private final CategoryValueService categoryValueService;
  private final ProjectService projectService;
  private final ProjectStepService projectStepService;
  private final TicketMapper ticketMapper;
  private final ObjectMapper objectMapper;

  public String generateTicketNumber() {
    int year = Year.now().getValue();
    String sequenceName = "ticket_number_" + year;

    if (ticketRepository.checkSequenceExists(sequenceName) == 0) {
      ticketQueryRepository.createSequence(sequenceName);
    }

    long nextTicketNumber = ticketRepository.getNextTicketNumber(sequenceName);
    return year + "-" + nextTicketNumber;
  }

  public TicketDTO create(TicketCreateRequestDTO body) {
    CategoryValueEntity ticketCategory =
        categoryValueService.getCategoryValueById(body.getCategoryValueId());
    CategoryValueEntity ticketPriority =
        categoryValueService.getCategoryValueById(body.getPriorityValueId());
    ProjectEntity projectEntity = projectService.getProjectEntityById(body.getProjectId());
    ProjectStepEntity stepEntity =
        projectStepService.getProjectStepEntityById(body.getProjectStepId());
    TicketEntity ticketEntity =
        TicketEntity.builder()
            .number(generateTicketNumber())
            .title(body.getTitle())
            .category(ticketCategory)
            .deadline(body.getDeadline())
            .priority(ticketPriority)
            .additionalDetails(body.getAdditionalDetails())
            .project(projectEntity)
            .step(stepEntity)
            .build();
    TicketEntity savedTicket = ticketRepository.save(ticketEntity);
    return ticketMapper.toDTO(savedTicket);
  }

  public TicketDTO getById(UUID id) {
    TicketEntity companyEntity = getTicketEntityById(id);
    return ticketMapper.toDTO(companyEntity);
  }

  public TicketEntity getTicketEntityById(UUID id) {
    return ticketRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MSG.TICKET_NOT_FOUND + id));
  }

  public SimpleResponse deleteById(UUID id) {
    TicketEntity projectToDelete = getTicketEntityById(id);
    projectToDelete.setIsDeleted(true);
    TicketEntity projectDeleted = ticketRepository.save(projectToDelete);
    String item = "ticket";
    if (projectDeleted.getIsDeleted()) {
      return SimpleResponse.builder().message(Shared.deleteMessage(item, id)).build();
    } else {
      return SimpleResponse.builder().message(Shared.cannotDeleteMessage(item, id)).build();
    }
  }

  public TicketDTO editById(UUID id, TicketEditRequestDTO body) {
    TicketEntity ticketToEdit = getTicketEntityById(id);
    CategoryValueEntity ticketCategory =
        categoryValueService.getCategoryValueById(body.getCategoryValueId());
    CategoryValueEntity ticketPriority =
        categoryValueService.getCategoryValueById(body.getPriorityValueId());
    ProjectEntity projectEntity = projectService.getProjectEntityById(body.getProjectId());
    ProjectStepEntity stepEntity =
        projectStepService.getProjectStepEntityById(body.getProjectStepId());
    ticketToEdit.setTitle(body.getTitle());
    ticketToEdit.setCategory(ticketCategory);
    ticketToEdit.setDeadline(body.getDeadline());
    ticketToEdit.setPriority(ticketPriority);
    ticketToEdit.setAdditionalDetails(body.getAdditionalDetails());
    ticketToEdit.setProject(projectEntity);
    ticketToEdit.setStep(stepEntity);
    TicketEntity updatedTicket = ticketRepository.save(ticketToEdit);
    return ticketMapper.toDTO(updatedTicket);
  }

  public TicketListResponseDTO filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    List<TicketRowDTO> results = ticketQueryRepository.getList(params);
    List<TicketDTO> projectDTOs = results.stream().map(ticketMapper::toDTO).toList();
    String contentRange =
        Shared.prepareContentRange(
            results.isEmpty() ? 0 : results.getFirst().getTotalElements(),
            params.getOffset(),
            params.getLimit());
    return TicketListResponseDTO.builder().data(projectDTOs).contentRange(contentRange).build();
  }
}
