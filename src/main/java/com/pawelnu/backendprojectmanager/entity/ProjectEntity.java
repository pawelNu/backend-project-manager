package com.pawelnu.backendprojectmanager.entity;

import com.pawelnu.backendprojectmanager.enumeration.DefinitionState;
import jakarta.persistence.*;
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

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "finished", nullable = false)
    @Enumerated(EnumType.STRING)
    private DefinitionState finished;

}
