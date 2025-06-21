package com.playground.chat.global.entity

import com.playground.chat.global.util.IdUtil
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.uuid.UuidValueGenerator
import java.util.*

class IdGenerator: UuidValueGenerator {
    override fun generateUuid(session: SharedSessionContractImplementor?): UUID? {
        return IdUtil.generateUuidV7()
    }
}
