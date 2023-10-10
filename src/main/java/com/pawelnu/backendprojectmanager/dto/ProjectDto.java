package com.pawelnu.backendprojectmanager.dto;

import com.pawelnu.backendprojectmanager.enumeration.DefinitionState;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDto {

    @NotEmpty(message = "Project name cannot be empty!")
    @Size(max = 255, message = "Project name must have max 255 characters!")
    private java.lang.String name;

    private DefinitionState finished;

}
