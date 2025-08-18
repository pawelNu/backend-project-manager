package com.pawelnu.projectmanager.endpoints.ticket.history;

import com.pawelnu.projectmanager.endpoints.ticket.TicketEntity;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketRowDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketHistoryMapper {

  @Mapping(source = "category.category.id", target = "categoryId")
  @Mapping(source = "category.category.name", target = "categoryName")
  @Mapping(source = "category.id", target = "categoryValueId")
  @Mapping(source = "category.stringValue", target = "categoryValue")
  @Mapping(source = "priority.category.id", target = "priorityId")
  @Mapping(source = "priority.category.name", target = "priorityName")
  @Mapping(source = "priority.id", target = "priorityValueId")
  @Mapping(source = "priority.stringValue", target = "priorityValue")
  @Mapping(source = "project.id", target = "projectId")
  @Mapping(source = "project.name", target = "projectName")
  @Mapping(source = "step.id", target = "projectStepId")
  @Mapping(source = "step.name", target = "projectStepName")
  TicketDTO toDTO(TicketEntity entity);

  TicketDTO toDTO(TicketRowDTO row);
}
