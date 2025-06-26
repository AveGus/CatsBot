package com.avegus.commons.model;

import lombok.Data;

@Data
public class CatAddRequest {
    private String name;
    private String fileId;
    private String creatorUsername;
    private Long creatorId;
}
