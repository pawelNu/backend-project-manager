package com.pawelnu.BackendProjectManager.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketEntity {

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

  //  TODO: why this is null?
  @Version private Integer version;
  @CreatedDate private LocalDateTime createdDate;
  @LastModifiedDate private LocalDateTime lastModificationDate;
}
