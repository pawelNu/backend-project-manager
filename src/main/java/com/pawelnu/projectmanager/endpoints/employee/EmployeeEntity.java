package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.endpoints.authority.EmployeeAuthorityEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String email;
  private String phoneNumber;

  //  @Enumerated(EnumType.STRING)
  //  private PersonRole role;

  //  @OneToMany(mappedBy = "registeringPerson")
  //  private List<TicketEntity> registeringTickets;
  //
  //  @OneToMany(mappedBy = "assignedPerson")
  //  private List<TicketEntity> assignedTickets;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private CompanyEntity company;

  @OneToMany(mappedBy = "employee")
  private Set<EmployeeAuthorityEntity> authorities = new HashSet<>();

  //  @ManyToMany(fetch = FetchType.EAGER)
  //  @JoinTable(
  //      name = "employee_authorities",
  //      joinColumns = @JoinColumn(name = "employee_id"),
  //      inverseJoinColumns = @JoinColumn(name = "authority_id"))
  //  private Set<AuthorityEntity> authorities = new HashSet<>();
}
