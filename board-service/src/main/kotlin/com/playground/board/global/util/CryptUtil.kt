package com.playground.board.global.util

import java.security.MessageDigest

class CryptUtil {
    class Sha512 {
        companion object {
            fun encrypt(input: String): String {
                val md = MessageDigest.getInstance("SHA-512")
                val byteData = md.digest(input.toByteArray())
                val sb = StringBuilder()

                for (byteDatum in byteData) {
                    sb.append(Integer.toHexString((byteDatum.toInt() and 0xff) + 0x100).substring(1))
                }

                return sb.toString()
            }
        }
    }
}
