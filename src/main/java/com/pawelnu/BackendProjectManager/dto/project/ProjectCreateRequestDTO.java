package com.pawelnu.BackendProjectManager.dto.project;

import lombok.Data;

@Data
public class ProjectCreateRequestDTO {

    private String name;

    private String status = "No";
}
