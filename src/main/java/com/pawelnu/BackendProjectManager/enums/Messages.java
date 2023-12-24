package com.pawelnu.BackendProjectManager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Messages {
    PROJECT_NOT_FOUND("Project not found with id: "),
    SORTING_FIELD_CANNOT_BE_NULL_OR_EMPTY("Sorting field cannot be empty or null!"),
    NOT_FOUND_SORTING_FIELD("Not found sorting field: ");

    private final String msg;
}
