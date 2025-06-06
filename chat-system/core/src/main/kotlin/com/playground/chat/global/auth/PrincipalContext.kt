package com.playground.chat.global.auth

object PrincipalContext {
    private val currentPrincipal = ThreadLocal<UserPrincipal>()

    fun <T> operate(principal: UserPrincipal?, function: () -> T): T {
        try {
            this.setPrincipal(principal)

            return function()
        } finally {
            this.clear()
        }
    }

    fun getPrincipal(): UserPrincipal? {
        return currentPrincipal.get()
    }

    fun setPrincipal(principal: UserPrincipal?) {
        currentPrincipal.set(principal)
    }

    private fun clear() {
        currentPrincipal.remove()
    }
}
