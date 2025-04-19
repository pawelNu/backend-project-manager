package com.pawelnu.projectmanager.entity;

import jakarta.persistence.*;
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

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private Long seriesNumber;
  private String title;

  @ManyToOne
  @JoinColumn(name = "registering_person_id")
  private PersonEntity registeringPerson;

  @ManyToOne
  @JoinColumn(name = "assigned_person_id")
  private PersonEntity assignedPerson;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private ProjectEntity project;

  @OneToOne(mappedBy = "parentTicket")
  private TicketHierarchyEntity parentTicket;

  @OneToMany(mappedBy = "childTicket")
  private List<TicketHierarchyEntity> childTickets;

  @OneToMany(mappedBy = "ticket")
  private List<TicketHistoryEntity> ticketHistories;
}
