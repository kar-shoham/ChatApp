package com.shoham.chat_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdOn;

    @Column(updatable = true, nullable = false)
    private LocalDateTime modifiedOn;

    @PrePersist
    public void prePersist()
    {
        this.createdOn = LocalDateTime.now();
        this.modifiedOn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate()
    {
        this.modifiedOn = LocalDateTime.now();
    }
}
