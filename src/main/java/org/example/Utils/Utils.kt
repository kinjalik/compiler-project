package org.example.Utils

object Utils {
    fun decToHex(number: Int, pad: Int): String {
        return number.toString(16).padStart(pad, '0').uppercase()
    }
}