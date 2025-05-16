package com.pawelnu.projectmanager.endpoints.ticket.history;

import com.pawelnu.projectmanager.endpoints.ticket.TicketEntity;
import com.pawelnu.projectmanager.endpoints.ticket.status.TicketStatusEntity;
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
@Table(name = "ticket_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistoryEntity extends Auditable {
  @Id @GeneratedValue UUID id;

  @ManyToOne
  @JoinColumn(name = "ticket_id")
  private TicketEntity ticket;

  @ManyToOne
  @JoinColumn(name = "status_id")
  private TicketStatusEntity status;
}
