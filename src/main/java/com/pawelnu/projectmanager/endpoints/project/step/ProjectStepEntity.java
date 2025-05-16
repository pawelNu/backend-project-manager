package com.pawelnu.projectmanager.endpoints.project.step;

import com.pawelnu.projectmanager.endpoints.project.step.comment.ProjectStepCommentEntity;
import com.pawelnu.projectmanager.endpoints.ticket.TicketEntity;
import com.pawelnu.projectmanager.endpoints.ticket.priority.TicketPriorityEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectStepEntity extends Auditable {
  @Id @GeneratedValue private UUID id;
  private String name;

  @ManyToOne
  @JoinColumn(name = "priority_id")
  private TicketPriorityEntity priority;

  private List<ProjectStepCommentEntity> comments;
  private List<TicketEntity> tickets;
  private Instant deadline;
}
