package com.playground.chat.global.entity

import com.playground.chat.global.util.UuidUtil
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.uuid.UuidValueGenerator
import java.util.UUID

class IdGenerator: UuidValueGenerator {
    override fun generateUuid(session: SharedSessionContractImplementor?): UUID? {
        return UuidUtil.generateUuidV7()
    }
}
