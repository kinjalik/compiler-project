package org.example.tokens

data class DigitToken(
    val value: Int
) : Token() {
    override fun toString(): String = String.format("DIGIT(%d)", value)
}
