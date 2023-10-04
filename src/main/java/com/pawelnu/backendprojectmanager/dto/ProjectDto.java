package com.pawelnu.backendprojectmanager.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDto {

    @NotEmpty(message = "Project name cannot be empty!")
//    TODO Add unique name validation
    private String name;

}
