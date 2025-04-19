package com.pawelnu.projectmanager.entity;

import com.pawelnu.projectmanager.enums.PersonRole;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String firstName;
  private String lastName;

  @Enumerated(EnumType.STRING)
  private PersonRole role;

  @OneToMany(mappedBy = "registeringPerson")
  private List<TicketEntity> registeringTickets;

  @OneToMany(mappedBy = "assignedPerson")
  private List<TicketEntity> assignedTickets;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private CompanyEntity company;
}
