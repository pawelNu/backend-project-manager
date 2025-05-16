package com.pawelnu.projectmanager.endpoints.ticket.attachment;

import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketAttachmentEntity extends Auditable {
  @Id @GeneratedValue UUID id;
  private String name;
  //  TODO store file in database or only link to file?
}
