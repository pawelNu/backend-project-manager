package com.pawelnu.BackendProjectManager.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IsFinished {

    NO("No"),
    YES("Yes");

    private final String value;
}
