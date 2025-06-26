package com.avegus.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCatRequest {
    private Long userId;
    private String catName;
    private String fileId;
}
