package com.pawelnu.backendprojectmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "project")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectEntity {

    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "name")
    private String name;
}
