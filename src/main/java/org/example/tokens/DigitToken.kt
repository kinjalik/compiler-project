package org.example.tokens

data class DigitToken(
    val value: Int
) : Token() {
    override fun toString() = String.format("DIGIT(%d)", value)
    override fun reverse() = value.toString()
}
