package com.pawelnu.backendprojectmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class APIResponse<T> {

    Integer recordCount;
    T response;
}
