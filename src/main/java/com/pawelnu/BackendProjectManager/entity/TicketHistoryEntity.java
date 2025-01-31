package com.pawelnu.BackendProjectManager.entity;

import com.pawelnu.BackendProjectManager.enums.TicketStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistoryEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "ticket_id")
  private TicketEntity ticket;

  private Instant date;

  @Enumerated(EnumType.STRING)
  private TicketStatus fromState;

  @Enumerated(EnumType.STRING)
  private TicketStatus toState;

  @ManyToOne
  @JoinColumn(name = "from_person_id")
  private PersonEntity fromPerson;

  @ManyToOne
  @JoinColumn(name = "to_person_id")
  private PersonEntity toPerson;

  private String comment;
}
