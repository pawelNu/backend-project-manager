// package com.pawelnu.projectmanager.entity;
//
// import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
// import com.pawelnu.projectmanager.enums.ProjectStatus;
// import jakarta.persistence.*;
// import java.util.List;
// import java.util.UUID;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;
//
// @Entity
// @Table(name = "projects")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class ProjectEntity extends Auditable {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
//  private UUID id;
//
//  private String name;
//
//  @Enumerated(value = EnumType.STRING)
//  private ProjectStatus projectStatus;
//
//  @ManyToOne
//  @JoinColumn(name = "company_id")
//  private CompanyEntity company;
//
//  @OneToMany(mappedBy = "project")
//  private List<TicketEntity> tickets;
// }
