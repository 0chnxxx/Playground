package com.playground.chat.global.entity

import com.playground.chat.global.auth.SecurityContext
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.Instant

class AuditEntityListener {
    @PrePersist
    fun prePersist(entity: AuditEntity) {
        entity.createdAt = Instant.now()
        entity.createdBy = getCurrentUser()
    }

    @PreUpdate
    fun preUpdate(entity: AuditEntity) {
        entity.updatedAt = Instant.now()
        entity.updatedBy = getCurrentUser()
    }

    private fun getCurrentUser(): String {
        val principal = SecurityContext.getPrincipal()

        return principal?.id.let { it?.toString() ?: "system" }
    }
}
