package com.pawelnu.projectmanager.endpoints.attachment;

import com.pawelnu.projectmanager.endpoints.project.ProjectEntity;
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
@Table(name = "attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentEntity extends Auditable {
  @Id @GeneratedValue UUID id;
  private String name;
  //  TODO store file in database or only link to file?
  private String pathToFile;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private ProjectEntity project;

  @ManyToOne
  @JoinColumn(name = "ticket_id")
  private TicketEntity ticket;
}
