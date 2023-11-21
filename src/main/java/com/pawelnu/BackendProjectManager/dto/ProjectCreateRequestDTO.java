package com.pawelnu.BackendProjectManager.dto;

import lombok.Data;

@Data
public class ProjectCreateRequestDTO {

    private String name;

    private String isFinished = "No";
}
