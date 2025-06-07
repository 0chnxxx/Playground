package com.playground.chat.global.util

import com.github.f4b6a3.uuid.UuidCreator
import java.util.*

object UuidUtil {
    fun generateUuidV7(): UUID {
        return UuidCreator.getTimeOrderedEpoch()
    }
}
