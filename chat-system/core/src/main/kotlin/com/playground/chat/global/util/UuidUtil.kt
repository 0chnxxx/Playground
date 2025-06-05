package com.playground.chat.global.util

import com.fasterxml.uuid.Generators
import java.util.UUID

object UuidUtil {
    fun generateUuidV7(): UUID {
        return Generators.timeBasedGenerator().generate()
    }
}
