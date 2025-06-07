package com.playground.chat.global.auth

object PrincipalContext {
    private val currentPrincipal = ThreadLocal<CustomPrincipal>()

    fun <T> operate(principal: CustomPrincipal?, function: () -> T, after: (() -> Unit)): T {
        try {
            this.setPrincipal(principal)

            return function()
        } finally {
            after.invoke()
        }
    }

    fun getPrincipal(): CustomPrincipal? {
        return currentPrincipal.get()
    }

    fun setPrincipal(principal: CustomPrincipal?) {
        currentPrincipal.set(principal)
    }

    fun clear() {
        currentPrincipal.remove()
    }
}
