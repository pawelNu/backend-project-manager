package com.pawelnu.BackendProjectManager.dto;

import com.pawelnu.BackendProjectManager.entity.enums.IsFinished;
import lombok.Data;

import java.util.UUID;

@Data
public class ProjectDTO {

    private UUID id;

    private String name;

    private IsFinished isFinished;

}
