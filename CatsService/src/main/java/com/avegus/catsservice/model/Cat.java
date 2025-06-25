package com.avegus.catsservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cats", schema = "cats")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cat {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_id", nullable = false)
    private String fileId;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "creator_username", nullable = false)
    private String creatorUsername;

    @Column(name = "likes_count", nullable = false)
    private Long likesCount;

    @Column(name = "dislikes_count", nullable = false)
    private Long dislikesCount;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    public static Cat create(String name, String fileId, String creatorUsername, Long creatorId) {
        return new Cat(
                UUID.randomUUID(),
                name,
                fileId,
                creatorId,
                creatorUsername,
                0L,
                0L,
                LocalDateTime.now()
        );
    }
}

