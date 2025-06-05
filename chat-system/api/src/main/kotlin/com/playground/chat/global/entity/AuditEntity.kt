package com.playground.chat.global.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditEntityListener::class)
abstract class AuditEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now()

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    var createdBy: String? = "system"

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: Instant? = null

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: String? = null

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false
}
