package com.pawelnu.projectmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "ticket_hierarchy")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHierarchyEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "parent_ticket_id")
  private TicketEntity parentTicket;

  @ManyToOne
  @JoinColumn(name = "child_ticket_id")
  private TicketEntity childTicket;

  private Integer level;
}
