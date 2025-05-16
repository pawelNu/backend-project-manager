package com.pawelnu.projectmanager.endpoints.project.stepcomment;

import com.pawelnu.projectmanager.endpoints.ticket.TicketEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_step_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectStepCommentEntity extends Auditable {
  @Id @GeneratedValue UUID id;
  private String comment;

  @ManyToOne
  @JoinColumn(name = "ticket_id")
  private TicketEntity ticket;
}
