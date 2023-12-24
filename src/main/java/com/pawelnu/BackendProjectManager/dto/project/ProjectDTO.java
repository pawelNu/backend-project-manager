package com.pawelnu.BackendProjectManager.dto.project;

import java.util.UUID;
import lombok.Data;

@Data
public class ProjectDTO {

    private UUID id;
    private String name;
    private String status;
}
