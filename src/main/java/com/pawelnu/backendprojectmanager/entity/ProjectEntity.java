package com.pawelnu.backendprojectmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "projects")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectEntity {

    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "name", unique = true)
    private String name;
}
