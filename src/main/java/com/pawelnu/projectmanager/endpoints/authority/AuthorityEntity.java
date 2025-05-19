package com.pawelnu.projectmanager.endpoints.authority;

import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityEntity extends Auditable {

  @Id @GeneratedValue private UUID id;
  private String name;

  @ManyToMany(mappedBy = "authority")
  private Set<EmployeeAuthorityEntity> employees = new HashSet<>();
}
// TODO create table category_types
// TODO create table type_values
// TODO create table companies
// TODO create table company_addresses
// TODO create table employees
// TODO create table authorities
// TODO create table employee_authorities
// TODO create table priorities
// TODO create table project_steps
// TODO create table project_step_comments
// TODO create table projects
// TODO create table tickets
// TODO create table attachments
// TODO create table ticket_histories
