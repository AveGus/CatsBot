package com.avegus.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CatDto {
    private UUID id;
    private String name;
    private String fileId;
    private Long creatorId;
    private String creatorUsername;
    private Long likesCount;
    private Long dislikesCount;
    private LocalDateTime created;
}
