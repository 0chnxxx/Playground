package com.playground.chat.global.auth

object SecurityContext {
    private val currentUser = ThreadLocal<UserPrincipal>()

    fun getPrincipal(): UserPrincipal? {
        return currentUser.get()
    }

    fun setPrincipal(principal: UserPrincipal) {
        currentUser.set(principal)
    }

    fun clear() {
        currentUser.remove()
    }
}
