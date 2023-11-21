package com.pawelnu.BackendProjectManager.entity.enums;

import com.pawelnu.BackendProjectManager.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IsFinished {
    NO("No"),
    YES("Yes");

    private final String value;

    public static IsFinished fromValue(String value) {
        for (IsFinished isFinished : values()) {
            if (isFinished.value.equalsIgnoreCase(value)) {
                return isFinished;
            }
        }
        throw new BadRequestException("Unsupported project status: " + value);
    }
}
