package com.playground.chat.global.entity

import com.playground.chat.global.auth.PrincipalContext
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.Instant

class AuditEntityListener {
    @PrePersist
    fun prePersist(entity: AuditEntity) {
        entity.createdAt = Instant.now()
        entity.createdBy = getCurrentPrincipal()
    }

    @PreUpdate
    fun preUpdate(entity: AuditEntity) {
        entity.updatedAt = Instant.now()
        entity.updatedBy = getCurrentPrincipal()
    }

    private fun getCurrentPrincipal(): String {
        val principal = PrincipalContext.getPrincipal()

        return principal?.id?.toString() ?: "system"
    }
}
