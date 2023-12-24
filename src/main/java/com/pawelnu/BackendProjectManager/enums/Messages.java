package com.pawelnu.BackendProjectManager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Messages {
    PROJECT_NOT_FOUND( "Project not found with id: "),
    PROJECT_SORTING_FIELD_CANNOT_BE_NULL_OR_EMPTY("Project's sorting field cannot be empty or null!");

    private final String msg;
}
