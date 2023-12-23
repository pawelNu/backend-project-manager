package com.pawelnu.BackendProjectManager.dto;

import com.pawelnu.BackendProjectManager.enums.PageValues;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagingAndSortingRequestDTO {

    private Integer pageNumber = PageValues.PAGE_NUMBER.getValue();
    private Integer pageSize = PageValues.PAGE_SIZE.getValue();
    @NotEmpty(message = "Sorting field cannot be empty")
    @NotNull(message = "Sorting field cannot be empty")
    // TODO adding the validation is not working
    //  check why this is not working
    //  java.lang.IllegalArgumentException: Property must not be null or empty
    private String sortingField;
    private Boolean isAscendingSorting;
}
