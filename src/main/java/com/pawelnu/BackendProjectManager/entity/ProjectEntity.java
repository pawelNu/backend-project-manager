package com.pawelnu.BackendProjectManager.entity;

import com.pawelnu.BackendProjectManager.entity.enums.IsFinished;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

    @Id
    private UUID id = UUID.randomUUID();

    private String name;

    @Enumerated(value = EnumType.STRING)
    private IsFinished isFinished;

}
