package org.example.Utils

import kotlin.math.ceil
import kotlin.math.log

object Utils {
    fun decToHex(number: Int, pad: Int): String {
        return number.toString(16).padStart(pad, '0').uppercase()
    }

    fun decodeByteBase(number: Int): Int{
        val x = ceil(log(number.toDouble(), 2.0)).toInt()
        return x / 8 + if (x % 8 == 0) {
            0
        } else {
            1
        }

    }
}