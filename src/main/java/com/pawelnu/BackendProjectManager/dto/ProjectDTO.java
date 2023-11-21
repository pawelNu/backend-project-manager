package com.pawelnu.BackendProjectManager.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProjectDTO {

    private UUID id;

    private String name;

    private String isFinished;

}
