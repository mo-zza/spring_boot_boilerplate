package com.mozza.springboilerplate.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36, nullable = false, updatable = false, unique = true, columnDefinition = "gen_random_uuid()")
    private UUID id;

    @CreatedDate
    @Column(updatable = false, columnDefinition = "now()")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(columnDefinition = "now()")
    private LocalDateTime modifiedDate;
}
