package org.example.tokens

data class AtomToken(
    val value: String
) : Token() {
    override fun toString(): String = String.format("ATOM(%s)", value)
}
