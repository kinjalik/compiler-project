package org.example.tokens

data class ParenthesisTokens(
    val type: ParenthesisType
) : Token() {
    override fun toString(): String = when (type) {
        ParenthesisType.LEFT -> "LPAREN"
        ParenthesisType.RIGHT -> "RPAREN"
    }
}

enum class ParenthesisType {
    LEFT, RIGHT
}
