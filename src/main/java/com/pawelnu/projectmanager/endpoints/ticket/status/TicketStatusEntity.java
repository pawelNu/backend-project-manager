package com.pawelnu.projectmanager.endpoints.ticket.status;

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
@Table(name = "ticket_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketStatusEntity extends Auditable {
  @Id @GeneratedValue UUID id;
  private String name;
}
