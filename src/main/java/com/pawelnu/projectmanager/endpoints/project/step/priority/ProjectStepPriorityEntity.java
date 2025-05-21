package com.pawelnu.projectmanager.endpoints.project.step.priority;

import com.pawelnu.projectmanager.endpoints.ticket.TicketEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "project_step_priorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectStepPriorityEntity extends Auditable { // TODO adjust entity to db schema
  @Id @GeneratedValue UUID id;
  private String name;

  @OneToMany(mappedBy = "priority")
  private List<TicketEntity> tickets;

  private Instant deadline;
}
