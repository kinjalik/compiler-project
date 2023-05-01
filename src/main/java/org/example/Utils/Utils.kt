package org.example.Utils

object Utils {
    fun decToHex(number: Int, pad: Int): String {
        return String.format("0x%${pad}X", number).substring(2)
    }
}