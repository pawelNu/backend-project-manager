package com.pawelnu.projectmanager.endpoints.ticket;

import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.project.ProjectEntity;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepEntity;
import com.pawelnu.projectmanager.endpoints.ticket.history.TicketHistoryEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketEntity extends Auditable {
  @Id @GeneratedValue UUID id;
  private String ticketNumber;
  private String title;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private CategoryValueEntity category;

  private Instant deadline;

  @ManyToOne
  @JoinColumn(name = "priority_id")
  private CategoryValueEntity priority;

  @OneToMany(mappedBy = "ticket")
  private List<TicketHistoryEntity> histories;

  private String additionalDetails;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private ProjectEntity project;

  @ManyToOne
  @JoinColumn(name = "project_step_id")
  private ProjectStepEntity step;
}
