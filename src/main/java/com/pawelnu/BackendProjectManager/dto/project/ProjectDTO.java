package com.pawelnu.BackendProjectManager.dto.project;

import lombok.Data;

import java.util.UUID;

@Data
public class ProjectDTO {

    private UUID id;
    private String name;
    private String status;
}
