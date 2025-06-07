package com.playground.chat.global.auth

object PrincipalContext {
    private val currentPrincipal = ThreadLocal<CustomPrincipal>()

    fun <T> operate(principal: CustomPrincipal?, function: () -> T): T {
        try {
            this.setPrincipal(principal)

            return function()
        } finally {
            this.clear()
        }
    }

    fun getPrincipal(): CustomPrincipal? {
        return currentPrincipal.get()
    }

    fun setPrincipal(principal: CustomPrincipal?) {
        currentPrincipal.set(principal)
    }

    private fun clear() {
        currentPrincipal.remove()
    }
}
