package com.pawelnu.BackendProjectManager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {

  @Id private UUID id = UUID.randomUUID();
  private String name;

  @OneToMany(mappedBy = "registeringPerson")
  private List<TicketEntity> registeringTickets;

  @OneToMany(mappedBy = "assignedPerson")
  private List<TicketEntity> assignedTickets;
}
