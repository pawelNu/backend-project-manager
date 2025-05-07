package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.endpoints.authority.AuthorityEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull private String firstName;
  @NotNull private String lastName;
  @NotNull private String username;
  @NotNull private String password;
  @NotNull private String email;
  @NotNull private String phoneNumber;

  //  @Enumerated(EnumType.STRING)
  //  private PersonRole role;

  //  @OneToMany(mappedBy = "registeringPerson")
  //  private List<TicketEntity> registeringTickets;
  //
  //  @OneToMany(mappedBy = "assignedPerson")
  //  private List<TicketEntity> assignedTickets;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "company_id")
  private CompanyEntity company;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "employee_authorities",
      joinColumns = @JoinColumn(name = "employee_id"),
      inverseJoinColumns = @JoinColumn(name = "authority_id"))
  private Set<AuthorityEntity> authorities = new HashSet<>();
}
